package magic;

import magic.data.CardDefinitions;
import magic.data.CubeDefinitions;
import magic.data.DeckGenerators;
import magic.data.DeckUtils;
import magic.data.DuelConfig;
import magic.data.GeneralConfig;
import magic.data.KeywordDefinitions;
import magic.model.MagicGameLog;
import magic.test.TestGameBuilder;
import magic.ui.MagicFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MagicMain {

    public static final String VERSION = "1.52";
    public static final String SOFTWARE_TITLE =
            "Magarena " + VERSION +
            (MagicUtility.isDevMode() ? " [DEV MODE]" : "");

    public static MagicFrame rootFrame;

    private static final String GAME_FOLDER  = "Magarena";
    private static final String MODS_PATH    = "mods";
    private static final String SCRIPTS_PATH = "scripts";
    private static final String LOGS_PATH = "logs";
    private static final String SAVED_DUELS_PATH = "duels";
    private static final String PLAYER_PROFILES_PATH = "players";
    private static final String AVATAR_SETS_PATH = "avatars";
    private static final String GAME_PATH =
            (System.getProperty("magarena.dir") != null ?
                    System.getProperty("magarena.dir") :
                        System.getProperty("user.dir")) + File.separatorChar + GAME_FOLDER;

    private static SplashScreen splash;

    public static void main(final String[] args) {

        // setup the handler for any uncaught exception
        Thread.setDefaultUncaughtExceptionHandler(new magic.model.MagicGameReport());

        splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            System.err.println("Error: no splash image specified on the command line");
        }

        System.out.println(getRuntimeParameters());
        parseCommandline(args);

        // setup the game log
        setSplashStatusMessage("Initializing log...");
        MagicGameLog.initialize();

        // show the data folder being used
        System.err.println("Data folder : "+GAME_PATH);

        // try to set the look and feel
        try {
            for (final LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

            // customize nimbus look
            UIManager.getLookAndFeelDefaults().put("Table.showGrid", true);
            // removes hardcoded border
            UIManager.getLookAndFeelDefaults().put("ScrollPane[Enabled].borderPainter", null);
        }
        catch (Exception e) {
            System.err.println("Unable to set look and feel. Probably missing the latest version of Java 6.");
            e.printStackTrace();
        }

        final long start_time = System.currentTimeMillis();
        setSplashStatusMessage("Initializing game engine...");
        initialize();
        final double duration = (double)(System.currentTimeMillis() - start_time) / 1000;
        System.err.println("Initalization of engine took " + duration + "s");

        setSplashStatusMessage("Starting UI...");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                startUI();
            }
        });
    }

    private static void startUI() {
        rootFrame = new MagicFrame(SOFTWARE_TITLE);
        rootFrame.showMainMenuScreen();
        // Add "-DtestGame=X" VM argument to start a TestGameBuilder game
        // where X is one of the classes (without the .java) in "magic.test".
        final String testGame = System.getProperty("testGame");
        if (testGame != null) {
            rootFrame.openGame(TestGameBuilder.buildGame(testGame));
        }
        if (MagicUtility.isAiVersusAi()) {
            final DuelConfig config = DuelConfig.getInstance();
            config.load();
            rootFrame.newDuel(config);
        }
    }

    private static void parseCommandline(final String[] args) {
        for (String arg : args) {
            switch (arg.toLowerCase()) {
            case "disablelogviewer":
                GeneralConfig.getInstance().setLogViewerDisabled(true);
                break;
            }
        }
    }

    public static String getGamePath() {
        return GAME_PATH;
    }

    public static String getGameFolder() {
        return GAME_FOLDER;
    }

    public static String getModsPath() {
        return getGamePath()+File.separatorChar+MODS_PATH;
    }

    public static String getScriptsPath() {
        return getGamePath()+File.separatorChar+SCRIPTS_PATH;
    }

    public static String getScriptsMissingPath() {
        return getGamePath() + File.separatorChar + "scripts_missing";
    }

    public static String getLogsPath() {
        return getDataPath(LOGS_PATH);
    }

    public static String getSavedDuelsPath() {
        return getDataPath(SAVED_DUELS_PATH);
    }

    public static String getPlayerProfilesPath() {
        return getDataPath(PLAYER_PROFILES_PATH);
    }

    public static String getAvatarSetsPath() {
        return getDataPath(AVATAR_SETS_PATH);
    }

    /**
     * Gets path to a specified data directory. If the directory does not exist it
     * attempts to create it. If that fails then it uses the GAME_PATH directory instead.
     */
    private static String getDataPath(final String dataDirectory) {
        final Path path = Paths.get(getGamePath()).resolve(dataDirectory);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
                return Paths.get(getGamePath()).toString();
            }
        }
        return path.toString();
    }

    static void initializeEngine() {
        CardDefinitions.loadCardDefinitions();
        if (Boolean.getBoolean("debug")) {
            setSplashStatusMessage("Loading card abilities...");
            CardDefinitions.loadCardAbilities();
        }
        setSplashStatusMessage("Loading cube definitions...");
        CubeDefinitions.loadCubeDefinitions();
        setSplashStatusMessage("Loading keyword definitions...");
        KeywordDefinitions.getInstance().loadKeywordDefinitions();
        setSplashStatusMessage("Loading deck generators...");
        DeckGenerators.getInstance().loadDeckGenerators();
    }

    private static void initialize() {
        final File gamePathFile = new File(getGamePath());
        if (!gamePathFile.exists() && !gamePathFile.mkdir()) {
            System.err.println("Unable to create directory " + getGamePath());
        }

        final File modsPathFile = new File(getModsPath());
        if (!modsPathFile.exists() && !modsPathFile.mkdir()) {
            System.err.println("Unable to create directory " + getModsPath());
        }

        DeckUtils.createDeckFolder();
        initializeEngine();
    }

    public static void setSplashStatusMessage(final String message) {
        if (splash != null) {
            try {
                final Graphics2D g2d = splash.createGraphics();
                // clear what we don't need from previous state
                g2d.setComposite(AlphaComposite.Clear);
                g2d.fillRect(0, 0, splash.getSize().width, splash.getSize().height);
                // draw new state
                g2d.setPaintMode();
                // draw message
                g2d.setColor(Color.WHITE);
                final Font f = new Font("Monospaced", Font.PLAIN, 16);
                g2d.setFont(f);
                g2d.drawString(MagicMain.SOFTWARE_TITLE, 10, 20);
                g2d.drawString(message, 10, 40);
                splash.update();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
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

}
