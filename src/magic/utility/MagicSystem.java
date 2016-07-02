package magic.utility;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import magic.data.CardDefinitions;
import magic.data.DeckGenerators;
import magic.data.GeneralConfig;
import magic.data.MagicCustomFormat;
import magic.data.UnimplementedParser;
import magic.model.MagicGameLog;
import magic.utility.MagicFileSystem.DataPath;

final public class MagicSystem {
    private MagicSystem() {}

    public static final String VERSION = "1.75";

    public static final String SOFTWARE_TITLE;
    private static final boolean DEV_MODE;
    static {
        DEV_MODE = Boolean.getBoolean("devMode") || Boolean.getBoolean("debug");
        SOFTWARE_TITLE = "Magarena " + VERSION + (DEV_MODE ? " [DEV MODE]" : "");
        System.setProperty("http.agent", SOFTWARE_TITLE);
    }

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

    public static void setIsTestGame(boolean b) {
        System.setProperty("testGame", b ? "Y" : "");
    }

    public static boolean isTestGame() {
        return (System.getProperty("testGame") != null && !System.getProperty("testGame").isEmpty());
    }

    public static boolean isDevMode() {
        return DEV_MODE;
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
            @Override
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
        MagicCustomFormat.loadCustomFormats();
        reporter.setMessage("Loading deck generators...");
        DeckGenerators.getInstance().loadDeckGenerators();

    }

    public static File getJarFile() throws URISyntaxException {

        CodeSource codeSource = MagicSystem.class.getProtectionDomain().getCodeSource();
        File jarFile = new File(codeSource.getLocation().toURI());

        if (jarFile.isFile() && jarFile.exists()) {
            return jarFile;
        } else if (System.getProperty("jarFile") != null) {
            jarFile = new File(System.getProperty("jarFile"));
            return jarFile;
        }

        return null;
    }

    /**
     * Restart the current Java application.
     * <p>
     * Should also work when JAR is not available (eg. when running from IDE).
     */
    public static void restart() throws URISyntaxException, IOException {

        final List<String> command = new ArrayList<>();

        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        command.add(javaBin);

        // vm arguments
        final List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        for (final String arg : vmArguments) {
            // if it's the agent argument : we ignore it otherwise the
            // address of the old application and the new one will be in conflict
            if (!arg.contains("-agentlib")) {
                command.add(arg);
            }
        }

        final File jarFile = getJarFile();
        if (jarFile != null) {
            command.add("-jar");
            command.add(jarFile.getPath());
        } else {
            // Sun property pointing to the main class and its arguments.
            // Might not be defined on non Hotspot VM implementations.
            command.add("-cp \"");
            command.add(System.getProperty("java.class.path"));
            command.add("\" ");
            command.add(System.getProperty("sun.java.command").split(" ")[0]);
        }

        // execute the command in a shutdown hook, to be sure that all the
        // resources have been disposed before restarting the application
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    final ProcessBuilder builder = new ProcessBuilder(command);
                    builder.start();
                } catch (final IOException ex) {
                    System.err.println(ex);
                }
            }
        });

        System.exit(0);

    }

    public static boolean isNewInstall() {
        return Files.exists(MagicFileSystem.getDataPath().resolve(GeneralConfig.CONFIG_FILENAME)) == false;
    }

}
