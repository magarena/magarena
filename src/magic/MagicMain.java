package magic;

import magic.utility.ProgressReporter;
import magic.ui.SplashProgressReporter;
import java.awt.SplashScreen;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import magic.data.DuelConfig;
import magic.data.GeneralConfig;
import magic.test.TestGameBuilder;
import magic.ui.ScreenController;
import magic.ui.UiExceptionHandler;
import magic.utility.MagicSystem;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import magic.model.player.PlayerProfiles;

public class MagicMain {

    private static SplashScreen splash;
    private static ProgressReporter reporter = new ProgressReporter();

    public static void main(final String[] args) {

        Thread.setDefaultUncaughtExceptionHandler(new UiExceptionHandler());

        setSplashScreen();
       
        System.out.println(MagicSystem.getRuntimeParameters());
        parseCommandline(args);

        // show the data folder being used
        System.out.println("Data folder : "+ MagicFileSystem.getDataPath());

        // init subsystems
        final long start_time = System.currentTimeMillis();
        reporter.setMessage("Initializing game engine...");
        MagicSystem.initialize(reporter);
        if (MagicSystem.showStartupStats()) {
            final double duration = (double)(System.currentTimeMillis() - start_time) / 1000;
            System.err.println("Initalization of engine took " + duration + "s");
        }
        
        // try to set the look and feel
        setLookAndFeel("Nimbus");
        reporter.setMessage("Starting UI...");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                startUI();
            }
        });
    }
        
    private static void setLookAndFeel(final String laf) {
        try {
            for (final LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (laf.equals(info.getName())) {
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
            reporter = new SplashProgressReporter(splash);
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

        // -DtestGame=X, where X is one of the classes (without the .java) in "magic.test".
        final String testGame = System.getProperty("testGame");
        if (testGame != null) {
            ScreenController.showDuelGameScreen(TestGameBuilder.buildGame(testGame));
            return;
        }

        // -DselfMode=true
        if (MagicSystem.isAiVersusAi()) {
            final DuelConfig config = DuelConfig.getInstance();
            config.load();
            
            // set both player profile to AI for AI vs AI mode
            config.setPlayerProfile(0, PlayerProfiles.getDefaultAiPlayer());
            config.setPlayerProfile(1, PlayerProfiles.getDefaultAiPlayer());

            ScreenController.getMainFrame().newDuel(config);
            return;
        }

        // normal UI startup.
        ScreenController.showStartScreen();
        
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
}
