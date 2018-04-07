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
import java.util.Locale;
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

    public static final String VERSION = "1.92";
    static {
        System.setProperty("http.agent", "Magarena " + VERSION);
    }

    public static final boolean IS_WINDOWS_OS = System.getProperty("os.name").toLowerCase(Locale.ENGLISH).startsWith("windows");
    private static final ProgressReporter reporter = new ProgressReporter();

    // Load card definitions in the background so that it does not delay the
    // loading of the UI. Override done() to ensure exceptions not suppressed.
    public static final FutureTask<Void> loadPlayable = new FutureTask<Void>(
            () -> initializeEngine(reporter), null
    ) {
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

    public static final FutureTask<Void> loadMissing = new FutureTask<Void>(
            () -> CardDefinitions.loadMissingCards(), null
    ) {
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

    public static String getVersionTitle() {
        return "Magarena " + VERSION + (isDevMode() ? " [DEV MODE]" : "");
    }

    public static void setIsTestGame(boolean b) {
        System.setProperty("testGame", b ? "Y" : "");
    }

    public static boolean isTestGame() {
        return !System.getProperty("testGame", "").isEmpty()
            || !System.getProperty("saveGame", "").isEmpty();
    }

    public static boolean isDevMode() {
        return Boolean.getBoolean("devMode") || Boolean.getBoolean("debug");
    }

    public static void setIsDevMode(boolean b) {
        System.setProperty("devMode", String.valueOf(b));
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

    public static void setAiVersusAi(boolean b) {
        System.setProperty("selfMode", String.valueOf(b));
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
        StringBuilder params = new StringBuilder();
        for (String param : bean.getInputArguments()) {
            params.append(param).append("\n");
        }
        return params.toString();
    }

    public static String getLoadingProgress() {
        return reporter.getMessage();
    }

    public static void waitForMissingCards() {
        if (loadMissing.isDone()) {
            return;
        } else {
            try {
                loadMissing.get();
            } catch (final InterruptedException|ExecutionException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void waitForPlayableCards() {
        if (loadPlayable.isDone()) {
            return;
        } else {
            try {
                loadPlayable.get();
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

        // must load config here otherwise annotated card image theme-specific
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

        // Queue up tasks to run synchronously on a single background thread.
        final ExecutorService background = Executors.newSingleThreadExecutor();
        background.execute(loadPlayable);
        background.execute(() -> CardDefinitions.postCardDefinitions());
        background.execute(loadMissing);
        background.shutdown();

        // if parse scripts missing or pre-load abilities then load cards synchronously
        if (isParseMissing() || isDebugMode()) {
            waitForPlayableCards();
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

    public static boolean isNotNormalGame() {
        return MagicSystem.isAiVersusAi()
            || MagicSystem.isDevMode()
            || MagicSystem.isTestGame();
    }

}
