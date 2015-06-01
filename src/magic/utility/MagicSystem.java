package magic.utility;

import magic.data.DeckGenerators;
import magic.data.KeywordDefinitions;
import magic.data.CubeDefinitions;
import magic.data.CardDefinitions;
import magic.data.UnimplementedParser;
import magic.data.GeneralConfig;
import magic.model.MagicGameLog;
import magic.utility.MagicFileSystem.DataPath;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ExecutionException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.io.File;

final public class MagicSystem {
    private MagicSystem() {}

    public static final boolean IS_WINDOWS_OS = System.getProperty("os.name").toLowerCase().startsWith("windows");
    private static final ProgressReporter reporter = new ProgressReporter();

    // Load card definitions in the background so that it does not delay the
    // loading of the UI. Override done() to ensure exceptions not suppressed.
    private static final FutureTask<Void> loadCards = new FutureTask<Void>(new Runnable() {
        @Override
        public void run() {
            initializeEngine(reporter);
        }
    }, null) {
        @Override
        protected void done() {
            try {
                if (!isCancelled()) {
                    get();
                }
            } catch (ExecutionException ex) {
                throw new RuntimeException(ex.getCause());
            } catch (InterruptedException ex) {
                // Shouldn't happen, we're invoked when computation is finished
                throw new AssertionError(ex);
            }
        }
    };

    public static boolean isTestGame() {
        return (System.getProperty("testGame") != null);
    }

    public static boolean isDevMode() {
        return Boolean.getBoolean("devMode") || isDebugMode();
    }

    public static boolean isDebugMode() {
        return Boolean.getBoolean("debug");
    }
    
    /**
     * add "-DparseMissing=true" VM argument for parsing scripts_missing folder.
     */
    public static boolean isParseMissing() {
        return Boolean.getBoolean("parseMissing");
    }

    /**
     * add "-DselfMode=true" VM argument for AI vs AI mode.
     */
    public static boolean isAiVersusAi() {
        return Boolean.getBoolean("selfMode");
    }

    /**
     * add "-DshowStats=true" to output startup statistics to console.
     */
    public static boolean showStartupStats() {
        return Boolean.getBoolean("showStats");
    }

    public static String getHeapUtilizationStats() {
        final int mb = 1024*1024;
        final Runtime runtime = Runtime.getRuntime();
        return "Used Memory: " + (runtime.totalMemory() - runtime.freeMemory()) / mb + "M" +
               "\nFree Memory: " + runtime.freeMemory() / mb  + "M" +
               "\nTotal Memory: " + runtime.totalMemory() / mb  + "M" +
               "\nMax Memory: " + runtime.maxMemory() / mb  + "M";
    }

    /**
     * Gets VM arguments.
     */
    public static String getRuntimeParameters() {
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        List<String> aList = bean.getInputArguments();
        String params = "";
        for (int i = 0; i < aList.size(); i++) {
            params += aList.get(i) + "\n";
        }
        return params;
    }

    public static String getLoadingProgress() {
        return reporter.getMessage();
    }

    public static void waitForAllCards() {
        if (loadCards.isDone()) {
            return;
        } else {
            try {
                loadCards.get();
            } catch (final InterruptedException|ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    
    private static void initializeEngine(final ProgressReporter reporter) {
        if (isParseMissing()) {
            UnimplementedParser.parseScriptsMissing(reporter);
            reporter.setMessage("Parsing card abilities...");
            UnimplementedParser.parseCardAbilities();

        }
        CardDefinitions.loadCardDefinitions(reporter);
    }

    public static void initialize(final ProgressReporter reporter) {

        // must load config here otherwise annotated card image theme-specifc
        // icons are not loaded before the AbilityIcon class is initialized
        // and you end up with the default icons instead.
        GeneralConfig.getInstance().load();
        
        final File gamePathFile = MagicFileSystem.getDataPath().toFile();
        if (!gamePathFile.exists() && !gamePathFile.mkdir()) {
            System.err.println("Unable to create directory " + gamePathFile.toString());
        }

        final File modsPathFile = MagicFileSystem.getDataPath(DataPath.MODS).toFile();
        if (!modsPathFile.exists() && !modsPathFile.mkdir()) {
            System.err.println("Unable to create directory " + modsPathFile.toString());
        }

        DeckUtils.createDeckFolder();

        // setup the game log
        reporter.setMessage("Initializing log...");
        MagicGameLog.initialize();
       
        // start a separate thread to load cards
        final ExecutorService background = Executors.newSingleThreadExecutor();
        background.execute(loadCards);
        background.execute(new Runnable() {
            public void run() {
                CardDefinitions.postCardDefinitions();
            }
        });
        background.shutdown();

        // if parse scripts missing or pre-load abilities then load cards synchronously
        if (isParseMissing() || isDebugMode()) {
            waitForAllCards();
        }
        
        if (isDebugMode()) {
            reporter.setMessage("Loading card abilities...");
            CardDefinitions.loadCardAbilities();
        }

        reporter.setMessage("Loading cube definitions...");
        CubeDefinitions.loadCubeDefinitions();
        reporter.setMessage("Loading deck generators...");
        DeckGenerators.getInstance().loadDeckGenerators();
        reporter.setMessage("Loading keyword definitions...");
        KeywordDefinitions.getInstance().loadKeywordDefinitions();
    }
}
