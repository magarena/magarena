package magic;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import magic.data.CardDefinitions;
import magic.data.CubeDefinitions;
import magic.data.DeckGenerators;
import magic.data.DeckUtils;
import magic.data.DuelConfig;
import magic.data.GeneralConfig;
import magic.data.KeywordDefinitions;
import magic.data.UnimplementedParser;
import magic.model.MagicGameLog;
import magic.test.TestGameBuilder;
import magic.ui.MagicFrame;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import magic.utility.MagicStyle;

public class MagicMain {

    public static final String VERSION = "1.57";
    public static final String SOFTWARE_TITLE =
            "Magarena " + VERSION +
            (MagicUtility.isDevMode() ? " [DEV MODE]" : "");

    public static MagicFrame rootFrame;           
    private static SplashScreen splash;

    public static void main(final String[] args) {

        // setup the handler for any uncaught exception
        Thread.setDefaultUncaughtExceptionHandler(new magic.model.MagicGameReport());

        setSplashScreen();
        
        System.out.println(getRuntimeParameters());
        parseCommandline(args);

        // setup the game log
        setSplashStatusMessage("Initializing log...");
        MagicGameLog.initialize();

        // show the data folder being used
        System.out.println("Data folder : "+ MagicFileSystem.getDataPath());

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

        // must load config here otherwise annotated card image theme-specifc
        // icons are not loaded before the AbilityIcon class is initialized
        // and you end up with the default icons instead.
        GeneralConfig.getInstance().load();

        initialize();
        if (MagicUtility.showStartupStats()) {
            final double duration = (double)(System.currentTimeMillis() - start_time) / 1000;
            System.err.println("Initalization of engine took " + duration + "s");
        }

        setSplashStatusMessage("Starting UI...");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                startUI();
            }
        });
    }

    /**
     * Sets splash screen as defined in JAR manifest or via "-splash" command line.
     * <p>
     * Can override with custom splash by placing "splash.png" in mods folder.
     */
    private static void setSplashScreen() {
        splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            System.err.println("Error: no splash image specified on the command line");
        } else {
            try {
                final File splashFile = MagicFileSystem.getDataPath(DataPath.MODS).resolve("splash.png").toFile();
                if (splashFile.exists()) {
                    splash.setImageURL(splashFile.toURI().toURL());
                }
            } catch (IOException ex) {
                // A problem occurred trying to set custom splash.
                // Log error and use default splash screen.
                System.err.println(ex);
            }
        }
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

    static void initializeEngine() {
        if (Boolean.getBoolean("parseMissing")) {
            UnimplementedParser.parseScriptsMissing();
            setSplashStatusMessage("Parsing card abilities...");
            UnimplementedParser.parseCardAbilities();
        }
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
        final File gamePathFile = MagicFileSystem.getDataPath().toFile();
        if (!gamePathFile.exists() && !gamePathFile.mkdir()) {
            System.err.println("Unable to create directory " + gamePathFile.toString());
        }

        final File modsPathFile = MagicFileSystem.getDataPath(DataPath.MODS).toFile();
        if (!modsPathFile.exists() && !modsPathFile.mkdir()) {
            System.err.println("Unable to create directory " + modsPathFile.toString());
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
                g2d.setColor(MagicStyle.getTranslucentColor(Color.BLACK, 100));
                g2d.fillRect(0, 0, splash.getSize().width, 22);
                g2d.fillRect(0, splash.getSize().height - 22, splash.getSize().width, 22);
                // draw message
                g2d.setColor(Color.WHITE);
                final Font f = new Font("Monospaced", Font.PLAIN, 16);
                g2d.setFont(f);
                // version
                final String version = "Version " + MagicMain.VERSION;
                int w = g2d.getFontMetrics(f).stringWidth(version);
                int x = (splash.getSize().width / 2) - (w / 2);
                g2d.drawString(version, x, 15);
                // status
                w = g2d.getFontMetrics(f).stringWidth(message);
                x = (splash.getSize().width / 2) - (w / 2);
                g2d.drawString(message, x, 274);
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
