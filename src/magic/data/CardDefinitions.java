package magic.data;

import magic.MagicMain;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicColor;
import magic.model.event.MagicTiming;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Load card definitions from cards.txt
 */
public class CardDefinitions {

    public static final String CARD_TEXT_FOLDER = "texts";
    public static final String CARD_IMAGE_FOLDER = "cards";
    public static final String TOKEN_IMAGE_FOLDER = "tokens";
    public static final String CARD_IMAGE_EXT = CardImagesProvider.IMAGE_EXTENSION;
    public static final String CARD_TEXT_EXT = ".txt";
    
    private static final List<MagicCardDefinition> cards = new ArrayList<MagicCardDefinition>();
    private static final List<MagicCardDefinition> landCards = new ArrayList<MagicCardDefinition>();
    private static final List<MagicCardDefinition> spellCards = new ArrayList<MagicCardDefinition>();
    private static final Map<String,MagicCardDefinition> cardsMap = new HashMap<String, MagicCardDefinition>();

    private static void setProperty(final MagicCardDefinition card,final String property,final String value) {
        try {
            CardProperty.valueOf(property.toUpperCase()).setProperty(card, value);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unsupported card property: " + property);
        }
    }
    
    private static void filterCards() {
        for (final MagicCardDefinition card : cards) {
            if (!card.isLand() && !card.isToken()) {
                spellCards.add(card);
            } else if (!card.isBasic() && !card.isToken()) {
                landCards.add(card);
            }
        }
    }

    private static void addDefinition(final MagicCardDefinition cardDefinition) {
        assert cardDefinition != null : "CardDefinitions.addDefinition passed null";
        assert cardDefinition.getIndex() == -1 : "cardDefinition has been assigned index";

        cardDefinition.setIndex(cards.size());
        cards.add(cardDefinition);
        cardsMap.put(cardDefinition.getFullName(),cardDefinition);

        //add to tokens or all (vintage) cube
        if (cardDefinition.isToken()) {
            TokenCardDefinitions.add(cardDefinition, cardDefinition.getFullName());
        } else {
            CubeDefinitions.getCubeDefinition("all").add(cardDefinition.getName());
        }
    }
    
    private static MagicCardDefinition prop2carddef(final Properties content) {
        final MagicCardDefinition cardDefinition=new MagicCardDefinition();

        //run through the list of properties
        for (String key : content.stringPropertyNames()) {
            setProperty(cardDefinition, key, content.getProperty(key));
        }

        //add card specific code
        if (cardDefinition.hasCode()) {
            addCardSpecificCode(cardDefinition);
        }

        return cardDefinition;
    }
        
    //link to companion object containing static variables
    private static void addCardSpecificCode(final MagicCardDefinition cardDefinition) {
        final String fname = cardDefinition.getFullName();
        final String cname = fname.replaceAll("[^A-Za-z]", "_");
        try { //reflection
            final Class c = Class.forName("magic.card." + cname);
            final Field[] fields = c.getDeclaredFields();
            for (final Field field : fields) {
                if (Modifier.isPublic(field.getModifiers())) {
                    final MagicChangeCardDefinition ccd = (MagicChangeCardDefinition)field.get(null);
                    ccd.change(cardDefinition);
                }
            }
        } catch (final ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (final IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private static void loadCardDefinition(final File file) {
        try {
            final MagicCardDefinition cdef = prop2carddef(FileIO.toProp(file));
            cdef.validate();
            addDefinition(cdef);
        } catch (final Throwable cause) {
            throw new RuntimeException("Error loading " + file, cause);
        }
    }
    
    public static void loadCardDefinitions() {
        //load all files in card directory
        final File cardDir = new File(MagicMain.getScriptsPath());
        final File[] files = cardDir.listFiles();
        for (File file : files) {
            loadCardDefinition(file);
        }

        filterCards();
        printStatistics();
        
        addDefinition(MagicCardDefinition.UNKNOWN);

        System.err.println(getNumberOfCards()+ " card definitions");
        MagicCardDefinition.printStatistics();
        
        // set card text
        loadCardTexts();
    }
    
    public static int getNumberOfCards() {
        return cards.size();
    }
    
    public static MagicCardDefinition getCard(final int cindex) {
        return cards.get(cindex);
    }
    
    public static MagicCardDefinition getCard(final String name) {
        final MagicCardDefinition cardDefinition=cardsMap.get(name);
        if (cardDefinition == null) {
            throw new RuntimeException("Unknown card: " + name);
        }
        return cardDefinition;
    }
    
    public static void loadCardTexts() {
        for(MagicCardDefinition card : getCards()) {
            if(card != MagicCardDefinition.UNKNOWN && card.getText().length() == 0) {
                // try to load text from file
                final StringBuilder buffer = new StringBuilder();
                buffer.append(MagicMain.getGamePath());
                buffer.append(File.separator);
                buffer.append(CARD_TEXT_FOLDER);
                buffer.append(File.separator);                
                buffer.append(card.getCardTextName());
                buffer.append(CARD_TEXT_EXT);
                
                try {
                    String text = FileIO.toStr(new File(buffer.toString()));
                    if(text != null) {
                        card.setText(text);                        
                    }
                } catch (IOException e) {
                    // text not downloaded or missing
                }
            }
        }
    }
        
    public static MagicCardDefinition getBasicLand(final MagicColor color) {
        if (MagicColor.Black.equals(color)) {
            return getCard("Swamp");
        } else if (MagicColor.Blue.equals(color)) {
            return getCard("Island");
        } else if (MagicColor.Green.equals(color)) {
            return getCard("Forest");
        } else if (MagicColor.Red.equals(color)) {
            return getCard("Mountain");
        } else if (MagicColor.White.equals(color)) {
            return getCard("Plains");
        }
        throw new RuntimeException("No matching basic land for MagicColor " + color);
    }

    public static List<MagicCardDefinition> getCards() {
        return cards;
    }
    
    public static List<MagicCardDefinition> getLandCards() {
        return landCards;
    }
    
    public static List<MagicCardDefinition> getSpellCards() {
        return spellCards;
    }
    
    private static void printStatistics() {
        final CardStatistics statistics=new CardStatistics(cards);
        statistics.printStatictics(System.err);
    }
}
