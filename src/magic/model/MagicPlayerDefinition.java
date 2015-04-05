package magic.model;

import magic.data.CardDefinitions;
import magic.data.DeckGenerators;
import magic.generator.RandomDeckGenerator;
import magic.model.player.AiPlayer;
import magic.model.player.PlayerProfile;
import magic.model.player.PlayerProfiles;

import java.util.Properties;

public class MagicPlayerDefinition {

    private static int nextAvatarIndex = 1;
    private static int MAX_AVATAR_INDEX = 10;

    private static final int DECK_SIZE=40;
    private static final int MIN_SOURCE=16;

    private static final String NAME="name";
    private static final String ARTIFICIAL="artificial";
    private static final String COLORS="colors";

    private String playerName;
    private boolean isAi;
    private MagicDeckProfile deckProfile;
    private final MagicDeck deck = new MagicDeck();
    private int avatarIndex;
    private final PlayerProfile playerProfile;

    // CTR
    MagicPlayerDefinition() { 
        playerProfile = null;
    }

    // CTR
    public MagicPlayerDefinition(final PlayerProfile aPlayerProfile, final MagicDeckProfile aDeckProfile) {
        playerName = aPlayerProfile.getPlayerName();
        isAi = aPlayerProfile.isArtificial();
        deckProfile = aDeckProfile;
        avatarIndex = getNextAvatarIndex();
        playerProfile = aPlayerProfile;
    }

    private static int getNextAvatarIndex() {
        nextAvatarIndex =  nextAvatarIndex > MAX_AVATAR_INDEX ? 1 : nextAvatarIndex + 1;
        return nextAvatarIndex;
    }
    
    public String getName() {
        return playerName;
    }

    public boolean isArtificial() {
        return isAi;
    }

    public void setDeckProfile(final MagicDeckProfile profile) {
        this.deckProfile=profile;
    }
    public MagicDeckProfile getDeckProfile() {
        return deckProfile;
    }

    private void addBasicLandsToDeck() {
        // Calculate statistics per color.
        final int[] colorCount=new int[MagicColor.NR_COLORS];
        final int[] colorSource=new int[MagicColor.NR_COLORS];
        for (final MagicCardDefinition cardDefinition : deck) {
            if (cardDefinition.isLand()) {
                for (final MagicColor color : MagicColor.values()) {
                    colorSource[color.ordinal()] += cardDefinition.getManaSource(color);
                }
            } else {
                final int colorFlags=cardDefinition.getColorFlags();
                for (final MagicColor color : deckProfile.getColors()) {
                    if (color.hasColor(colorFlags)) {
                        colorCount[color.ordinal()]++;
                    }
                }
            }
        }

        // Add optimal basic lands to deck.
        while (deck.size()<DECK_SIZE) {
            MagicColor bestColor=null;
            int lowestRatio=Integer.MAX_VALUE;
            for (final MagicColor color : MagicColor.values()) {
                final int index=color.ordinal();
                final int count=colorCount[index];
                if (count>0) {
                    final int source=colorSource[index];
                    final int ratio;
                    if (source<MIN_SOURCE) {
                        ratio=source-count;
                    } else {
                        ratio=source*100/count;
                    }
                    if (ratio<lowestRatio) {
                        lowestRatio=ratio;
                        bestColor=color;
                    }
                }
            }
            // fix for issue 446 (http://code.google.com/p/magarena/issues/detail?id=446).
            if (bestColor == null) {
                bestColor = MagicColor.getColor(MagicColor.getRandomColors(1).charAt(0));
            }
            final MagicCardDefinition landCard = CardDefinitions.getBasicLand(bestColor);
            colorSource[bestColor.ordinal()] += landCard.getManaSource(bestColor);
            deck.add(landCard);
        }
    }

    public MagicDeck getDeck() {
        return deck;
    }

    public void setDeck(final MagicDeck aDeck) {
        deck.setContent(aDeck);
    }

    public RandomDeckGenerator getDeckGenerator() {
        final String name = getDeckProfile().getDeckGeneratorName();

        if (name == null) {
            return null;
        }

        return DeckGenerators.getInstance().getDeckGenerator(name);
    }

    public void generateDeck(final RandomDeckGenerator defaultGenerator) {
        final RandomDeckGenerator customGenerator =  getDeckGenerator();

        if (customGenerator == null) {
            defaultGenerator.generateDeck(DECK_SIZE, deckProfile, deck);
        } else {
            customGenerator.generateDeck(DECK_SIZE, deckProfile, deck);
        }

        addBasicLandsToDeck();
    }

    private static String getDeckPrefix(final String prefix,final int index) {
        return prefix+"deck"+index;
    }

    void load(final Properties properties,final String prefix) {
        playerName=properties.getProperty(prefix+NAME,"");
        isAi=Boolean.parseBoolean(properties.getProperty(prefix+ARTIFICIAL,"true"));
        final String colors=properties.getProperty(prefix+COLORS,"");
        deckProfile=new MagicDeckProfile(colors);
        deck.clear();
        for (int index=1;index<=properties.size();index++) {
            final String deckPrefix = getDeckPrefix(prefix,index);
            if (properties.containsKey(deckPrefix)) {
                final String tName = properties.getProperty(deckPrefix);
                final MagicCardDefinition cdef = CardDefinitions.getCard(tName);
                if (cdef.isValid()){
                    deck.add(cdef);
                }
            }
        }
    }

    void save(final Properties properties,final String prefix) {
        properties.setProperty(prefix+NAME,playerName);
        properties.setProperty(prefix+ARTIFICIAL,Boolean.toString(isAi));

        int index=1;
        for (final MagicCardDefinition cardDefinition : deck) {
            properties.setProperty(getDeckPrefix(prefix,index++),cardDefinition.getFullName());
        }
    }

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    public int getAvatarIndex() {
        return avatarIndex;
    }

}
