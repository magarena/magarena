package magic.utility;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import magic.MagicMain;
import magic.data.CardDefinitions;
import magic.data.DeckGenerators;
import magic.data.GeneralConfig;
import magic.data.KeywordDefinitions;
import magic.data.MagicCustomFormat;
import magic.data.UnimplementedParser;
import magic.model.MagicGameLog;
import magic.utility.MagicFileSystem.DataPath;

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
        reporter.setMessage("Loading keyword definitions...");
        KeywordDefinitions.getInstance().loadKeywordDefinitions();
    }

    public static File getJarFile() throws URISyntaxException {
        
        CodeSource codeSource = MagicMain.class.getProtectionDomain().getCodeSource();
        File jarFile = new File(codeSource.getLocation().toURI().getPath());

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
     * Tested to work when running via Netbeans IDE.
     *
     * @param runBeforeRestart some custom code to be run before restarting
     */
    public static void restart(final Runnable runBeforeRestart) {

        /**
        * Sun property pointing to the main class and its arguments.
        * Might not be defined on non Hotspot VM implementations.
        */
        final String SUN_JAVA_COMMAND = "sun.java.command";

        try {
            // java binary
            final String java = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";

            // vm arguments
            final List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
            final StringBuffer vmArgsOneLine = new StringBuffer();
            for (final String arg : vmArguments) {
                // if it's the agent argument : we ignore it otherwise the
                // address of the old application and the new one will be in conflict
                if (!arg.contains("-agentlib")) {
                    vmArgsOneLine.append(arg);
                    vmArgsOneLine.append(" ");
                }
            }
            // init the command to execute, add the vm args
            final StringBuffer cmd = new StringBuffer("\"" + java + "\" " + vmArgsOneLine);

            // program main and program arguments
            final String[] mainCommand = System.getProperty(SUN_JAVA_COMMAND).split(" ");
            // program main is a jar
            if (mainCommand[0].endsWith(".jar")) {
                // if it's a jar, add -jar mainJar
                cmd.append("-jar ").append(new File(mainCommand[0]).getPath());
            } else {
                // else it's a .class, add the classpath and mainClass
                cmd.append("-cp \"").append(System.getProperty("java.class.path")).append("\" ").append(mainCommand[0]);
            }
            // finally add program arguments
            for (int i = 1; i < mainCommand.length; i++) {
                cmd.append(" ");
                cmd.append(mainCommand[i]);
            }
            // execute the command in a shutdown hook, to be sure that all the
            // resources have been disposed before restarting the application
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        Runtime.getRuntime().exec(cmd.toString());
                    } catch (final IOException e) {
                        //e.printStackTrace();
                    }
                }
            });
            // execute some custom code before restarting
            if (runBeforeRestart != null) {
                runBeforeRestart.run();
            }
            // exit
            System.exit(0);
        } catch (final Exception ex) {
            //ErrorViewer.showError(ex, "Restart \"%s\" exception", "");
        }
    }

}
