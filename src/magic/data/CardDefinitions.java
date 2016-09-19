package magic.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import groovy.lang.GroovyShell;
import groovy.transform.CompileStatic;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicColor;
import magic.model.event.MagicHandCastActivation;
import magic.utility.FileIO;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import magic.utility.MagicResources;
import magic.utility.MagicSystem;
import magic.utility.ProgressReporter;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

public class CardDefinitions {

    private static final File CARDS_SNAPSHOT_FILE =
            MagicFileSystem.getDataPath().resolve("snapshot.dat").toFile();

    private static final File SCRIPTS_DIRECTORY =
            MagicFileSystem.getDataPath(DataPath.SCRIPTS).toFile();

    // A MagicCardDefinition is a bit of a misnomer in that it represents a single
    // playable aspect of a card. For example, double faced or flip cards will be
    // represented by two MagicCardDefinitions, one for each of the faces or aspects
    // of that card that can be played.

    // Contains reference to all playable MagicCardDefinitions indexed by card name.
    private static final ConcurrentMap<String, MagicCardDefinition> allPlayableCardDefs = new ConcurrentHashMap<>();

    private static Map<String, MagicCardDefinition> missingCards = null;

    private static final AtomicInteger cdefIndex = new AtomicInteger(1);

    // groovy shell for evaluating groovy card scripts with autmatic imports
    private static final GroovyShell shell = new GroovyShell(
        new CompilerConfiguration().addCompilationCustomizers(
            new ImportCustomizer()
            .addStarImports(
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
                "magic.model.phase",
                "magic.card"
            ).addStaticStars(
                "magic.model.target.MagicTargetFilterFactory",
                "magic.model.choice.MagicTargetChoice"
            ),
            new ASTTransformationCustomizer(CompileStatic.class)
        )
    );

    private static void setProperty(final MagicCardDefinition card,final String property,final String value) {
        try {
            CardProperty.valueOf(property.toUpperCase()).setProperty(card, value);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("unknown card property value \"" + property + "\" = \"" + value + "\"");
        }
    }

    private static void addDefinition(final MagicCardDefinition cardDef) {
        assert cardDef != null : "CardDefinitions.addDefinition passed null";
        assert cardDef.getIndex() == -1 : "cardDefinition has been assigned index";

        cardDef.setIndex(cdefIndex.getAndIncrement());

        if (cardDef.isPlayable()) {
            cardDef.add(new MagicHandCastActivation(cardDef));
        }

        allPlayableCardDefs.put(cardDef.getAsciiName(), cardDef);
    }

    private static MagicCardDefinition prop2carddef(final File scriptFile, final boolean isMissing) {
        final Properties content = FileIO.toProp(scriptFile);
        final MagicCardDefinition cardDefinition = new MagicCardDefinition();

        if (isMissing) {
            cardDefinition.setInvalid();
        }

        for (final String key : content.stringPropertyNames()) {
            try {
                setProperty(cardDefinition, key, content.getProperty(key));
            } catch (Exception e) {
                if (isMissing) {
                    cardDefinition.setInvalid();
                } else {
                    throw e;
                }
            }
        }

        try {
            cardDefinition.validate();
        } catch (Exception e) {
            if (isMissing) {
                cardDefinition.setInvalid();
            } else {
                throw e;
            }
        }

        return cardDefinition;
    }

    //link to groovy script that returns array of MagicChangeCardDefinition objects
    static void addCardSpecificGroovyCode(final MagicCardDefinition cardDefinition, final String cardName) {
        try {
            final File groovyFile = new File(SCRIPTS_DIRECTORY, getCanonicalName(cardName) + ".groovy");
            if (!groovyFile.isFile()) {
                throw new RuntimeException("groovy file not found: " + groovyFile);
            }
            @SuppressWarnings("unchecked")
            final List<MagicChangeCardDefinition> defs = (List<MagicChangeCardDefinition>)shell.evaluate(groovyFile);
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
        return Normalizer.normalize(fullName, Form.NFD)
                         .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                         .replace("\u00C6", "AE");
    }

    private static void loadCardDefinition(final File file) {
        try {
            final MagicCardDefinition cdef = prop2carddef(file, false);
            addDefinition(cdef);
        } catch (final Throwable cause) {
            if (MagicSystem.isParseMissing()) {
                System.out.println("ERROR file: " + file + " cause: " + cause.getMessage());
            } else {
                throw new RuntimeException("Error loading " + file, cause);
            }
        }
    }

    public static void loadCardDefinition(final String cardName) {
        final File cardFile = new File(SCRIPTS_DIRECTORY, getCanonicalName(cardName) + ".txt");
        if (!cardFile.isFile()) {
            throw new RuntimeException("card script file not found: " + cardFile);
        }
        loadCardDefinition(cardFile);
    }

    /**
     * loads playable cards.
     */
    public static void loadCardDefinitions(final ProgressReporter reporter) {

        reporter.setMessage("Sorting card script files...");
        final File[] scriptFiles = MagicFileSystem.getSortedScriptFiles(SCRIPTS_DIRECTORY);

        reporter.setMessage("Loading cards...0%");
        final double totalFiles = scriptFiles.length;
        int fileCount = 0;
        for (final File file : scriptFiles) {
            loadCardDefinition(file);
            //
            // display percentage complete message every 10%.
            final double percentageComplete = (fileCount++ / totalFiles) * 100;
            final double m = percentageComplete % 10d;
            if (isZero(m, 0.01d)) {
                // This should only be called ten times.
                // It can have a serious effect on load time if called too many times.
                reporter.setMessage("Loading cards..." + ((int)percentageComplete + 10) + "%");
            }
        }
        reporter.setMessage("Loading cards...100%");

    }

    public static void postCardDefinitions() {
        printStatistics();
        updateNewCardsLog(loadCardsSnapshotFile());
    }

    private static boolean isZero(double value, double delta){
        return value >= -delta && value <= delta;
    }

    public static void loadCardAbilities() {
        final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Stream.concat(getDefaultPlayableCardDefStream(), getTokensCardDefStream()).forEach(cdef -> {
            executor.execute(() -> {
                try {
                    cdef.loadAbilities();
                    if (MagicSystem.isParseMissing() && !cdef.isToken()) {
                        System.out.println("OK card: " + cdef);
                    }
                } catch (Throwable cause) {
                    if (MagicSystem.isParseMissing()) {
                        System.out.println("ERROR card: " + cdef + " cause: " + cause.getMessage());
                    } else {
                        throw new RuntimeException("Unable to load " + cdef, cause);
                    }
                }
            });
        });
        executor.shutdown();
        try {
            executor.awaitTermination(100, TimeUnit.SECONDS);
        } catch (final InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static MagicCardDefinition getToken(final String original) {
        final MagicCardDefinition token = getCard(original);
        if (token.isToken()) {
            return token;
        } else {
            throw new RuntimeException("unknown token: \"" + original + "\"");
        }
    }

    public static MagicCardDefinition getMissingOrCard(final String original) {
        final String key = getASCII(original);
        return missingCards != null && missingCards.containsKey(key) ? missingCards.get(key) : getCard(original);
    }

    public static MagicCardDefinition getCard(final String original) {
        final String key = getASCII(original);
        // lazy loading of card scripts
        if (!allPlayableCardDefs.containsKey(key)) {
            loadCardDefinition(original);
        }
        if (allPlayableCardDefs.containsKey(key)) {
            return allPlayableCardDefs.get(key);
        } else {
            throw new RuntimeException("unknown card: \"" + original + "\"");
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

    private static Stream<MagicCardDefinition> getDefaultPlayableCardDefStream() {
        return getAllPlayableCardDefs().stream()
            .filter(MagicCardDefinition::isPlayable);
    }

    private static Stream<MagicCardDefinition> getTokensCardDefStream() {
        return getAllPlayableCardDefs().stream()
            .filter(MagicCardDefinition::isToken);
    }

    /**
     * Returns a list of all playable MagicCardDefinitions except those classed as hidden.
     * <p>
     * Only contains reference to the main MagicCardDefinition aspect of a card. This is
     * required for functions like the Deck Editor where you should not be able to select
     * the reverse side of a double-side card, for example.
     */
    public static List<MagicCardDefinition> getDefaultPlayableCardDefs() {
        return getDefaultPlayableCardDefStream()
            .collect(Collectors.toList());
    }

    /**
     * Returns a list all playable MagicCardDefinitions INCLUDING those classed as hidden.
     */
    public static Collection<MagicCardDefinition> getAllPlayableCardDefs() {
        MagicSystem.waitForAllCards();
        return allPlayableCardDefs.values();
    }

    public static synchronized List<MagicCardDefinition> getAllCards() {
        final List<MagicCardDefinition> combined = new ArrayList<>();
        combined.addAll(getAllPlayableCardDefs());
        combined.addAll(getMissingCards());
        return combined;
    }

    public static Stream<MagicCardDefinition> getNonBasicLandCards() {
        return getDefaultPlayableCardDefStream()
            .filter(card -> card.isLand() && !card.isBasic());
    }

    public static List<MagicCardDefinition> getSpellCards() {
        return getDefaultPlayableCardDefStream()
            .filter(card -> !card.isLand())
            .collect(Collectors.toList());
    }

    private static void printStatistics() {
        if (MagicSystem.showStartupStats()) {
            final CardStatistics statistics=new CardStatistics(getDefaultPlayableCardDefs());
            statistics.printStatictics(System.err);
        }
    }

    /**
     * Returns a list of card names which have yet to be implemented.
     * <p>
     * {@code cardsMap} contains a list of current playable cards.
     * {@code AllCardsNames.txt} contains the name of every possible playable card.
     * The difference is a list of unimplemented cards.
     */
    public static List<String> getMissingCardNames() throws IOException {
        final List<String> missingCardNames = new ArrayList<>();
        final InputStream stream = MagicResources.getAllCardNames();
        try (final Scanner sc = new Scanner(stream, FileIO.UTF8.name())) {
            while (sc.hasNextLine()) {
                final String cardName = sc.nextLine();
                if (!allPlayableCardDefs.containsKey(getASCII(cardName))) {
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
                    missingScripts.put(cdef.getAsciiName(), cdef);
                }
            }
        }

        missingCards = new HashMap<>();
        for (String cardName : missingCardNames) {
            final String cardKey = getASCII(cardName);
            if (missingScripts.containsKey(cardKey)) {
                missingCards.put(cardKey, missingScripts.get(cardKey));
            } else {
                final MagicCardDefinition card = new MagicCardDefinition();
                card.setName(cardName);
                card.setDistinctName(cardName);
                card.setInvalid();
                missingCards.put(cardKey, card);
            }
        }

    }

    /**
     * Gets a sorted list of all the script files in the "missing" folder.
     */
    private static File[] getSortedMissingScriptFiles() {
        final Path cardsPath = MagicFileSystem.getDataPath(DataPath.SCRIPTS_MISSING);
        final File[] files = cardsPath.toFile().listFiles((dir, name) -> {
            return name.toLowerCase().endsWith(".txt");
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
        new Thread(() -> {
            GeneralConfig.getInstance().setIsMissingFiles(isMissingPlayableImages());
        }).start();
    }

    public static boolean isMissingPlayableImages() {
        final Date aDate = GeneralConfig.getInstance().getPlayableImagesDownloadDate();
        return getAllPlayableCardDefs().stream()
            .filter(MagicCardDefinition::hasImageUrl)
            .anyMatch(card -> card.isImageUpdatedAfter(aDate) || card.isImageFileMissing());
    }

    public static String getScriptFilename(final MagicCardDefinition card) {
        return card.getFilename() + ".txt";
    }

    public static String getGroovyFilename(final MagicCardDefinition card) {
        return card.getFilename() + ".groovy";
    }

    public static boolean isCardPlayable(MagicCardDefinition card) {
        return allPlayableCardDefs.containsKey(card.getAsciiName());
    }

    public static boolean isCardMissing(MagicCardDefinition card) {
        return missingCards != null && missingCards.containsKey(card.getAsciiName());
    }

    public static boolean isPotential(MagicCardDefinition card) {
        return card.hasStatus() ? isCardMissing(card) && !card.getStatus().contains("not supported") : isCardMissing(card);
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
        if (CARDS_SNAPSHOT_FILE.exists()) {
            return MagicFileSystem.deserializeStringList(CARDS_SNAPSHOT_FILE);
        } else {
            saveCardsSnapshotFile();
            return new ArrayList<>();
        }
    }

    private static List<String> getPlayableNonTokenCardNames() {
        return getAllPlayableCardDefs().stream().filter(card -> !card.isToken()).map(MagicCardDefinition::getName).collect(Collectors.toList());
    }

    public static void updateNewCardsLog(final List<String> snapshot) {
        final List<String> cardNames = getPlayableNonTokenCardNames();
        cardNames.removeAll(snapshot);
        if (!cardNames.isEmpty()) {
            saveNewCardsLog(cardNames);
            saveCardsSnapshotFile();
        }
    }

    private static void saveNewCardsLog(final Collection<String> cardNames) {
        final Path LOGS_PATH = MagicFileSystem.getDataPath(DataPath.LOGS);
        final File LOG_FILE = LOGS_PATH.resolve("newcards.log").toFile();
        try (final PrintWriter writer = new PrintWriter(LOG_FILE)) {
            cardNames.forEach(writer::println);
        } catch (FileNotFoundException ex) {
            System.err.println("Failed to save " + LOG_FILE + " - " + ex);
        }
    }
}
