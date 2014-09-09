package magic.data;

import groovy.lang.GroovyShell;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import magic.MagicMain;
import magic.MagicUtility;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicColor;
import magic.model.event.MagicCardActivation;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

public class CardDefinitions {

    private static final File CARDS_SNAPSHOT_FILE =
            MagicFileSystem.getDataPath().resolve("snapshot.dat").toFile();

    private static final File SCRIPTS_DIRECTORY =
            MagicFileSystem.getDataPath(DataPath.SCRIPTS).toFile();

    private static final List<MagicCardDefinition> playableCards = new ArrayList<>();
    private static Map<String, MagicCardDefinition> missingCards = null;
    private static final List<MagicCardDefinition> landCards = new ArrayList<>();
    private static final List<MagicCardDefinition> spellCards = new ArrayList<>();
    private static final Map<String,MagicCardDefinition> cardsMap = new HashMap<>();

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
            throw new RuntimeException("unknown card property value \"" + property + "\" = \"" + value + "\"");
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
                    cardDefinition.setIsValid(false);
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
                new File(SCRIPTS_DIRECTORY, getCanonicalName(cardName) + ".groovy")
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
                         .replace("\u00C6", "AE");
    }

    private static void loadCardDefinition(final File file) {
        try {
            final MagicCardDefinition cdef = prop2carddef(file, false);
            cdef.validate();
            addDefinition(cdef);
        } catch (final Throwable cause) {
            //System.out.println("ERROR file: " + file + " cause: " + cause.getMessage());
            throw new RuntimeException("Error loading " + file, cause);
        }
    }

    /**
     * loads playable cards.
     */
    public static void loadCardDefinitions() {

        MagicMain.setSplashStatusMessage("Initializing cards database...");
        final File[] scriptFiles = getSortedScriptFiles(SCRIPTS_DIRECTORY);

        MagicMain.setSplashStatusMessage("Loading " +  getNonTokenCardsCount(scriptFiles) + " playable cards...");
        for (final File file : scriptFiles) {
            loadCardDefinition(file);
        }
        filterCards();
        printStatistics();
        updateNewCardsLog(loadCardsSnapshotFile());
    }

    public static File[] getSortedScriptFiles(final File scriptsDirectory) {
        final File[] files = scriptsDirectory.listFiles(new FilenameFilter() {
            @Override
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
        for (final MagicCardDefinition cdef : getPlayableCards()) {
            //skip hidden cards as their abilities will be loaded from their normal card definition
            if (cdef.isHidden()) {
                continue;
            }
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        cdef.loadAbilities();
                    } catch (Throwable cause) {
                        //System.out.println("ERROR card: " + cdef + " cause: " + cause.getMessage());
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

    public static MagicCardDefinition getCard(final String original) {
        final String name = getASCII(original);
        MagicCardDefinition cardDefinition = cardsMap.get(name);
        if (cardDefinition == null) {
            throw new RuntimeException("unknown card: \"" + original + "\"");
        } else {
            return cardDefinition;
        }
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

    public static List<MagicCardDefinition> getPlayableCards() {
        return playableCards;
    }

    public static List<MagicCardDefinition> getLandCards() {
        return landCards;
    }

    public static List<MagicCardDefinition> getSpellCards() {
        return spellCards;
    }

    private static void printStatistics() {
        if (MagicUtility.showStartupStats()) {
            final CardStatistics statistics=new CardStatistics(playableCards);
            statistics.printStatictics(System.err);
        }
    }

    public static synchronized List<MagicCardDefinition> getAllCards() {
        final List<MagicCardDefinition> combined = new ArrayList<>();
        combined.addAll(playableCards);
        combined.addAll(getMissingCards());
        return combined;
    }

    /**
     * Returns a list of card names which have yet to be implemented.
     * <p>
     * {@code cardsMap} contains a list of current playable cards.
     * {@code AllCardsNames.txt} contains the name of every possible playable card.
     * The difference is a list of missing cards.
     */
    public static List<String> getMissingCardNames() throws IOException {
        final List<String> missingCardNames = new ArrayList<>();
        final InputStream stream = MagicMain.class.getResourceAsStream("/magic/data/AllCardNames.txt");
        try (final Scanner sc = new Scanner(stream, FileIO.UTF8.name())) {
            while (sc.hasNextLine()) {
                final String cardName = sc.nextLine().trim();
                if (!cardsMap.containsKey(getASCII(cardName))) {
                    missingCardNames.add(cardName);
                }
            }
        }
        return missingCardNames;
    }

    private static void loadMissingCards(final List<String> missingCardNames) {

        final HashMap<String, MagicCardDefinition> missingScripts = new HashMap<>();

        if (GeneralConfig.getInstance().showMissingCardData()) {
            final File[] scriptFiles = getSortedMissingScriptFiles();
            if (scriptFiles != null) {
                for (final File file : scriptFiles) {
                    MagicCardDefinition cdef = prop2carddef(file, true);
                    missingScripts.put(getASCII(cdef.getFullName()), cdef);
                }
            }
        }

        missingCards = new HashMap<String, MagicCardDefinition>();
        for (String cardName : missingCardNames) {
            final String cardKey = getASCII(cardName);
            if (missingScripts.containsKey(cardKey)) {
                missingCards.put(cardKey, missingScripts.get(cardKey));
            } else {
                final MagicCardDefinition card = new MagicCardDefinition();
                card.setName(cardName);
                card.setFullName(cardName);
                card.setIsMissing(true);
                card.setIsValid(false);
                card.setIsScriptFileMissing(true);
                missingCards.put(cardKey, card);
            }
        }

    }

    /**
     * Gets a sorted list of all the script files in the "missing" folder.
     */
    private static File[] getSortedMissingScriptFiles() {
        final Path cardsPath = MagicFileSystem.getDataPath(DataPath.SCRIPTS_MISSING);
        final File[] files = cardsPath.toFile().listFiles(new FilenameFilter() {
            @Override
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

    public static void checkForMissingFiles() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GeneralConfig.getInstance().setIsMissingFiles(isMissingImages());
            }
        }).start();
    }

    public static boolean isMissingImages() {
        for (final MagicCardDefinition card : getPlayableCards()) {
            if (card.getImageURL() != null) {
                if (!MagicFileSystem.getCardImageFile(card).exists()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getScriptFilename(final MagicCardDefinition card) {
        return getCanonicalName(card.getName()) + ".txt";
    }

    public static String getGroovyFilename(final MagicCardDefinition card) {
        return getCanonicalName(card.getName()) + ".groovy";
    }

    public static boolean isCardPlayable(MagicCardDefinition card) {
        final String key = getASCII(card.getFullName());
        return cardsMap.containsKey(key);
    }

    public static boolean isCardMissing(MagicCardDefinition card) {
        final String key = getASCII(card.getFullName());
        return (missingCards == null ? false : missingCards.containsKey(key));
    }

    public static synchronized Collection<MagicCardDefinition> getMissingCards() {
        if (missingCards == null) {
            try {
                loadMissingCards(getMissingCardNames());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return missingCards.values();
    }

    private static void saveCardsSnapshotFile() {
        MagicFileSystem.serializeStringList(getPlayableNonTokenCardNames(), CARDS_SNAPSHOT_FILE);
    }

    private static List<String> loadCardsSnapshotFile() {
        if (!CARDS_SNAPSHOT_FILE.exists()) {
            saveCardsSnapshotFile();
            return new ArrayList<>();
        } else {
            return MagicFileSystem.deserializeStringList(CARDS_SNAPSHOT_FILE);
        }
    }

    private static List<String> getPlayableNonTokenCardNames() {
        final ArrayList<String> cardNames = new ArrayList<>();
        for (MagicCardDefinition card : playableCards) {
            if (!card.isToken()) {
                cardNames.add(card.getName());
            }
        }
        return cardNames;
    }

    public static void updateNewCardsLog(final List<String> snapshot) {
        final List<String> cardNames = getPlayableNonTokenCardNames();
        cardNames.removeAll(snapshot);
        if (cardNames.size() > 0) {
            saveNewCardsLog(cardNames);
            saveCardsSnapshotFile();
        }
    }

    private static void saveNewCardsLog(final Collection<String> cardNames) {
        final Path LOGS_PATH = MagicFileSystem.getDataPath(DataPath.LOGS);
        final File LOG_FILE = LOGS_PATH.resolve("newcards.log").toFile();
        try (final PrintWriter writer = new PrintWriter(LOG_FILE)) {
            for (String cardName : cardNames) {
                writer.println(cardName);
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Failed to save " + LOG_FILE + " - " + ex);
        }
    }
}
