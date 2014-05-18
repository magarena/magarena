package magic.data;

import magic.MagicMain;
import magic.MagicUtility;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicColor;
import magic.model.event.MagicCardActivation;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.text.Normalizer;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import groovy.lang.GroovyShell;

/**
 * Load card definitions from cards.txt
 */
public class CardDefinitions {

    public static final String CARD_IMAGE_FOLDER = "cards";
    public static final String TOKEN_IMAGE_FOLDER = "tokens";
    public static final String CARD_IMAGE_EXT = CardImagesProvider.IMAGE_EXTENSION;
    public static final String CARD_TEXT_EXT = ".txt";

    private static final List<MagicCardDefinition> playableCards = new ArrayList<MagicCardDefinition>();
    private static Map<String, MagicCardDefinition> missingCards = null;
    private static final List<MagicCardDefinition> landCards = new ArrayList<MagicCardDefinition>();
    private static final List<MagicCardDefinition> spellCards = new ArrayList<MagicCardDefinition>();
    private static final Map<String,MagicCardDefinition> cardsMap = new HashMap<String, MagicCardDefinition>();
    private static final File cardDir = new File(MagicMain.getScriptsPath());



    // groovy shell for evaluating groovy card scripts with autmatic imports
    private static final GroovyShell shell = new GroovyShell(
        new CompilerConfiguration().addCompilationCustomizers(
            new ImportCustomizer().addStarImports(
                "java.util",
                "magic.data",
                "magic.model",
                "magic.model.action",
                "magic.model.choice",
                "magic.model.condition",
                "magic.model.event",
                "magic.model.mstatic",
                "magic.model.stack",
                "magic.model.target",
                "magic.model.trigger",
                "magic.card"
            ),
            new ASTTransformationCustomizer(groovy.transform.CompileStatic.class)
        )
    );

    private static void setProperty(final MagicCardDefinition card,final String property,final String value) {
        try {
            CardProperty.valueOf(property.toUpperCase()).setProperty(card, value);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unsupported card property: " + property, e);
        }
    }

    private static void filterCards() {
        for (final MagicCardDefinition card : playableCards) {
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

        cardDefinition.setIndex(playableCards.size());
        playableCards.add(cardDefinition);
        final String key = getASCII(cardDefinition.getFullName());
        cardsMap.put(key,cardDefinition);

        //add to tokens or all (vintage) cube
        if (cardDefinition.isToken()) {
            TokenCardDefinitions.add(cardDefinition);
        } else {
            cardDefinition.add(new MagicCardActivation(cardDefinition));
            CubeDefinitions.getCubeDefinition("all").add(cardDefinition.getName());
        }
    }

    private static MagicCardDefinition prop2carddef(final File scriptFile, final boolean isMissing) {

        final Properties content = FileIO.toProp(scriptFile);
        final MagicCardDefinition cardDefinition = new MagicCardDefinition();
        cardDefinition.setIsMissing(isMissing);

        for (final String key : content.stringPropertyNames()) {
            try {
                setProperty(cardDefinition, key, content.getProperty(key));
            } catch (Exception e) {
                if (isMissing) {
                    if (MagicUtility.isDevMode() || MagicUtility.isDebugMode()) {
                        System.err.println(scriptFile.getName() + " [" + key + "] : "  + e.getMessage());
                    }
                } else {
                    throw new RuntimeException(e);
                }
            }
        }

        return cardDefinition;
    }

    //link to groovy script that returns array of MagicChangeCardDefinition objects
    static void addCardSpecificGroovyCode(final MagicCardDefinition cardDefinition, final String cardName) {
        try {
            @SuppressWarnings("unchecked")
            final List<MagicChangeCardDefinition> defs = (List<MagicChangeCardDefinition>)shell.evaluate(
                new File(cardDir, getCanonicalName(cardName) + ".groovy")
            );
            for (MagicChangeCardDefinition ccd : defs) {
                ccd.change(cardDefinition);
            }
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getCanonicalName(String fullName) {
        return fullName.replaceAll("[^A-Za-z0-9]", "_");
    }
    
    public static String getASCII(String fullName) {
        return Normalizer.normalize(fullName, Normalizer.Form.NFD)
                         .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                         .replace("Æ", "AE");
    }

    private static void loadCardDefinition(final File file) {
        try {
            final MagicCardDefinition cdef = prop2carddef(file, false);
            cdef.validate();
            addDefinition(cdef);
        } catch (final Throwable cause) {
            throw new RuntimeException("Error loading " + file, cause);
        }
    }

    /**
     * loads playable cards.
     */
    public static void loadCardDefinitions() {

        MagicMain.setSplashStatusMessage("Initializing cards database...");
        final File[] scriptFiles = getSortedScriptFiles();

        MagicMain.setSplashStatusMessage("Loading " +  getNonTokenCardsCount(scriptFiles) + " playable cards...");
        for (final File file : scriptFiles) {
            loadCardDefinition(file);
        }
        filterCards();
        printStatistics();
        addDefinition(MagicCardDefinition.UNKNOWN);

    }

    /**
     * Gets a sorted list of all the card script files in the "cards" folder.
     */
    private static File[] getSortedScriptFiles() {
        final File[] files = cardDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".txt");
            }
        });
        Arrays.sort(files);
        return files;
    }

    /**
     * Returns the number of non-token cards.
     * <p>
     * Assumes that token card contains "token" in the file name.
     */
    private static int getNonTokenCardsCount(final File[] files) {
        int count = 0;
        final Iterator<File> filesIterator = Arrays.asList(files).iterator();
        while (filesIterator.hasNext()) {
            final File f = filesIterator.next();
            if (!f.getName().toLowerCase().contains("token")) {
                count++;
            }
        }
        return count;
    }

    public static void loadCardAbilities() {
        final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (final MagicCardDefinition cdef : getCards()) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        cdef.loadAbilities();
                    } catch (Throwable cause) {
                        throw new RuntimeException("Unable to load " + cdef, cause);
                    }
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(100, TimeUnit.SECONDS);
        } catch (final InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static int getNumberOfCards() {
        return playableCards.size();
    }

    public static MagicCardDefinition getCard(final int cindex) {
        return playableCards.get(cindex);
    }

    public static MagicCardDefinition getCard(final String original) {
        final String name = getASCII(original);
        final MagicCardDefinition cardDefinition=cardsMap.get(name);
        if (cardDefinition == null) {
            throw new RuntimeException("Unknown card: " + original);
        }
        return cardDefinition;
    }

    public static MagicCardDefinition getBasicLand(final MagicColor color) {
        if (color == MagicColor.Black) {
            return getCard("Swamp");
        } else if (color == MagicColor.Blue) {
            return getCard("Island");
        } else if (color == MagicColor.Green) {
            return getCard("Forest");
        } else if (color == MagicColor.Red) {
            return getCard("Mountain");
        } else if (color == MagicColor.White) {
            return getCard("Plains");
        }
        throw new RuntimeException("No matching basic land for MagicColor " + color);
    }

    public static List<MagicCardDefinition> getCards() {
        return playableCards;
    }

    public static List<MagicCardDefinition> getLandCards() {
        return landCards;
    }

    public static List<MagicCardDefinition> getSpellCards() {
        return spellCards;
    }

    private static void printStatistics() {
        final CardStatistics statistics=new CardStatistics(playableCards);
        statistics.printStatictics(System.err);
    }

    public static List<MagicCardDefinition> getAllCards() {
        final List<MagicCardDefinition> combined = new ArrayList<MagicCardDefinition>();
        if (missingCards == null) {
            try {
                loadMissingCards(getMissingCardNames());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        combined.addAll(playableCards);
        combined.addAll(missingCards.values());
        return combined;
    }

    public static List<String> getMissingCardNames() throws IOException {
        final List<String> missingCardNames = new ArrayList<String>();
        final String content = FileIO.toStr(MagicMain.class.getResourceAsStream("/magic/data/AllCardNames.txt"));
        try (final Scanner sc = new Scanner(content)) {
            while (sc.hasNextLine()) {
                final String cardName = sc.nextLine().trim();
                if (!cardsMap.containsKey(cardName)) {
                    missingCardNames.add(cardName);
                }
            }
        }
        return missingCardNames;
    }

    private static void loadMissingCards(final List<String> missingCardNames) {

        final HashMap<String, MagicCardDefinition> missingScripts =
                new HashMap<String, MagicCardDefinition>();

        if (GeneralConfig.getInstance().showMissingCardData()) {
            final File[] scriptFiles = getSortedMissingScriptFiles();
            if (scriptFiles != null) {
                for (final File file : scriptFiles) {
                    MagicCardDefinition cdef = null;
                    cdef = prop2carddef(file, true);
                    missingScripts.put(cdef.getName(), cdef);
                }
            }
        }

        missingCards = new HashMap<String, MagicCardDefinition>();
        for (String cardName : missingCardNames) {
            if (missingScripts.containsKey(cardName)) {
                missingCards.put(cardName, missingScripts.get(cardName));
            } else {
                final MagicCardDefinition card = new MagicCardDefinition();
                card.setName(cardName);
                card.setFullName(cardName);
                card.setIsMissing(true);
                missingCards.put(cardName, card);
            }
        }

    }

    /**
     * Gets a sorted list of all the script files in the "missing" folder.
     */
    private static File[] getSortedMissingScriptFiles() {
        final Path cardsPath = Paths.get(MagicMain.getScriptsMissingPath());
        final File[] files = cardsPath.toFile().listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".txt");
            }
        });
        if (files != null) {
            Arrays.sort(files);
        }
        return files;
    }

    public static void resetMissingCardData() {
        if (missingCards != null) {
            missingCards.clear();
            missingCards = null;
        }
    }

}
