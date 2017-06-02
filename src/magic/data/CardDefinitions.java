package magic.data;

import groovy.lang.GroovyShell;
import groovy.transform.CompileStatic;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicColor;
import magic.model.event.MagicHandCastActivation;
import magic.ui.MagicCardImages;
import magic.ui.screen.images.download.CardImageDisplayMode;
import magic.utility.FileIO;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
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

    private static final GeneralConfig CONFIG = GeneralConfig.getInstance();

    // A MagicCardDefinition is a bit of a misnomer in that it represents a single
    // playable aspect of a card. For example, double faced or flip cards will be
    // represented by two MagicCardDefinitions, one for each of the faces or aspects
    // of that card that can be played.

    // Contains reference to all playable MagicCardDefinitions indexed by card name.
    private static final Map<String, MagicCardDefinition> allPlayableCardDefs = new ConcurrentHashMap<>();

    private static final Map<String, MagicCardDefinition> missingCards = new ConcurrentHashMap<>();

    private static final AtomicInteger cdefIndex = new AtomicInteger(1);

    static {
        CompilerConfiguration.DEFAULT.getOptimizationOptions().put(CompilerConfiguration.INVOKEDYNAMIC, Boolean.TRUE);
    }

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
            CardProperty.valueOf(property.toUpperCase(Locale.ENGLISH)).setProperty(card, value);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("unknown card property value \"" + property + "\" = \"" + value + "\"", e);
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
        return fullName.replace("\u00C6", "Ae").replaceAll("[^A-Za-z0-9]", "_");
    }

    public static String getASCII(String fullName) {
        return Normalizer.normalize(fullName, Form.NFD)
                         .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                         .replace("\u00C6", "Ae");
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

    public static MagicCardDefinition getToken(final int p, final int t, final String name) {
        return MagicCardDefinition.token(CardDefinitions.getToken(name), cdef -> cdef.setPowerToughness(p, t));
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
        return missingCards.containsKey(key) ? missingCards.get(key) : getCard(original);
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

    public static void loadMissingCards() {
        final File[] scriptFiles = getSortedMissingScriptFiles();
        if (scriptFiles != null) {
            for (final File file : scriptFiles) {
                MagicCardDefinition cdef = prop2carddef(file, true);
                missingCards.put(cdef.getAsciiName(), cdef);
            }
        }
    }

    /**
     * Gets a sorted list of all the script files in the "missing" folder.
     */
    private static File[] getSortedMissingScriptFiles() {
        final Path cardsPath = MagicFileSystem.getDataPath(DataPath.SCRIPTS_MISSING);
        final File[] files = cardsPath.toFile().listFiles((dir, name) -> {
            return name.toLowerCase(Locale.ENGLISH).endsWith(".txt");
        });
        if (files != null) {
            Arrays.sort(files);
        }
        return files;
    }

    public static boolean requiresNewImageDownload(MagicCardDefinition card, Date lastDownloadDate) {
        if (!card.hasImageUrl()) {
            return false;
        }
        if (MagicCardImages.isCustomCardImageFound(card)) {
            return false;
        }
        if (card.isImageUpdatedAfter(lastDownloadDate)) {
            return true;
        }
        if (CONFIG.getCardImageDisplayMode() == CardImageDisplayMode.PRINTED) {
            return MagicCardImages.isPrintedCardImageMissing(card);
        } else { // PROXY
            return MagicCardImages.isCroppedCardImageMissing(card)
                && MagicCardImages.isPrintedCardImageMissing(card);
        }
    }

    public static boolean isMissingPlayableImages() {
        Date aDate = CONFIG.getPlayableImagesDownloadDate();
        return getAllPlayableCardDefs().stream()
            .anyMatch(card -> requiresNewImageDownload(card, aDate));
    }

    public static void checkForMissingFiles() {
        new Thread(() -> {
            CONFIG.setIsMissingFiles(isMissingPlayableImages());
        }).start();
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
        return missingCards.containsKey(card.getAsciiName());
    }

    public static boolean isPotential(MagicCardDefinition card) {
        return card.hasStatus() ? isCardMissing(card) && !card.getStatus().contains("not supported") : isCardMissing(card);
    }

    public static Collection<MagicCardDefinition> getMissingCards() {
        MagicSystem.waitForMissingCards();
        return missingCards.values();
    }

    public static List<String> getMissingCardNames() {
        List<String> names = new ArrayList<String>(getMissingCards().size());
        for (final MagicCardDefinition cdef : getMissingCards()) {
            names.add(cdef.getName());
        }
        return names;
    }


    private static void saveCardsSnapshotFile() {
        MagicFileSystem.serializeStringList(getPlayableNonTokenCardNames(), CARDS_SNAPSHOT_FILE);
    }

    private static List<String> loadCardsSnapshotFile() {
        if (CARDS_SNAPSHOT_FILE.exists()) {
            try {
                return MagicFileSystem.deserializeStringList(CARDS_SNAPSHOT_FILE);
            } catch (Exception ex) {
                Logger.getLogger(CardDefinitions.class.getName()).log(Level.WARNING, null, ex);
                return new ArrayList<>();
            }
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
