package magic;

import java.awt.SplashScreen;
import java.io.File;
import java.io.IOException;
import javax.swing.SwingUtilities;
import magic.ai.MagicAI;
import magic.data.DuelConfig;
import magic.game.state.GameLoader;
import magic.model.player.AiProfile;
import magic.test.TestGameBuilder;
import magic.ui.ScreenController;
import magic.ui.SplashProgressReporter;
import magic.ui.UiExceptionHandler;
import magic.ui.helpers.LaFHelper;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import magic.utility.MagicSystem;
import magic.utility.ProgressReporter;

public class MagicMain {

    private static SplashScreen splash;
    private static ProgressReporter reporter = new ProgressReporter();
    private static CommandLineArgs cmdline;

    public static void main(final String[] args) {

        Thread.setDefaultUncaughtExceptionHandler(new UiExceptionHandler());

        setSplashScreen();

        System.out.println(MagicSystem.getRuntimeParameters());
        cmdline = new CommandLineArgs(args);

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

        LaFHelper.setDefaultLookAndFeel();

        reporter.setMessage("Starting UI...");
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

        // -DsaveGame=X, where X is the path to a snapshot.game
        final String saveGame = System.getProperty("saveGame");
        if (saveGame != null) {
            final File f = new File(saveGame);
            ScreenController.showDuelGameScreen(GameLoader.loadSavedGame(f));
            return;
        }

        // AI vs AI game.
        if (MagicSystem.isAiVersusAi()) {

            System.out.println("");
            System.err.println("=== AI vs AI ===");

            final DuelConfig config = DuelConfig.getInstance();
            config.load();
            config.setPlayerProfile(0, AiProfile.create(cmdline.getAi1(), cmdline.getAi1Level()));
            config.setPlayerProfile(1, AiProfile.create(cmdline.getAi2(), cmdline.getAi2Level()));

            System.out.println("P1 : " + config.getPlayerProfile(0).getPlayerLabel());
            System.out.println("P2 : " + config.getPlayerProfile(1).getPlayerLabel());
            System.out.println("AI threads : " + MagicAI.getMaxThreads());

            ScreenController.getFrame().newDuel(config);
            return;
        }

        // normal UI startup.
        ScreenController.showStartScreen();

    }
}
