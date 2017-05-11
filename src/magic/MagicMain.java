package magic;

import java.awt.SplashScreen;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import magic.ai.MagicAI;
import magic.data.DeckGenerators;
import magic.data.DuelConfig;
import magic.exception.InvalidDeckException;
import magic.game.state.GameLoader;
import magic.model.DuelPlayerConfig;
import magic.model.MagicDeck;
import magic.model.player.AiProfile;
import magic.test.TestGameBuilder;
import magic.ui.ScreenController;
import magic.ui.SplashProgressReporter;
import magic.ui.UiExceptionHandler;
import magic.ui.WikiPage;
import magic.ui.helpers.LaFHelper;
import magic.ui.widget.duel.animation.MagicAnimations;
import magic.utility.DeckUtils;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;
import magic.utility.MagicSystem;
import magic.utility.ProgressReporter;

public class MagicMain {

    private static final Logger LOGGER = Logger.getLogger(MagicMain.class.getName());

    private static SplashScreen splash;
    private static ProgressReporter reporter = new ProgressReporter();

    public static void main(final String[] args) {

        Thread.setDefaultUncaughtExceptionHandler(new UiExceptionHandler());

        setSplashScreen();

        System.out.println(MagicSystem.getRuntimeParameters());

        final CommandLineArgs cmdline = new CommandLineArgs(args);
        parseCommandLine(cmdline);

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
        SwingUtilities.invokeLater(() -> { startUI(cmdline); });
    }

    private static void parseCommandLine(CommandLineArgs cmdline) {

        if (cmdline.showHelp()) {
            System.err.println("--help specified - opening wiki page and exit...\n" + WikiPage.COMMAND_LINE.getUrl());
            WikiPage.show(WikiPage.COMMAND_LINE);
            System.exit(0);
        }

        MagicAnimations.setEnabled(cmdline.isAnimationsEnabled());
        MagicAI.setMaxThreads(cmdline.getMaxThreads());
        MagicSystem.setIsDevMode(cmdline.isDevMode());
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

    private static void setPlayerDeck(String deckArg, DuelPlayerConfig player) {

        if (deckArg.isEmpty()) {
            player.setDeckProfile("Random;***");
            DeckGenerators.setRandomDeck(player);
            return;

        } else if (deckArg.equals("@")) { // random deck file.
            player.setDeckProfile("Random;@");
            DeckGenerators.setRandomDeck(player);
            return;

        } else if (deckArg.equals("#")) { // random single color deck.
            player.setDeckProfile("Random;*");
            DeckGenerators.setRandomDeck(player);
            return;

        } else if (deckArg.equals("##")) { //random two-color deck.
            player.setDeckProfile("Random;**");
            DeckGenerators.setRandomDeck(player);
            return;

        } else if (deckArg.equals("###")) { // random three-color deck.
            player.setDeckProfile("Random;***");
            DeckGenerators.setRandomDeck(player);
            return;

        } else if (!deckArg.isEmpty()) { // search for deck file.
            File deckFile = DeckUtils.findDeckFile(deckArg);
            if (deckFile != null) {
                MagicDeck deck = DeckUtils.loadDeckFromFile(deckFile.toPath());
                if (deck.isValid()) {
                    player.setDeck(deck);
                    return;
                }
            }
        }

        throw new InvalidDeckException("Invalid deck specified in command line : " + deckArg);
    }

    private static void runAivsAiGame(CommandLineArgs cmdline) {
        System.out.println("");
        System.err.println("=== AI vs AI ===");

        final DuelConfig config = DuelConfig.getInstance();
        config.load();
        config.setPlayerProfile(0, AiProfile.create(cmdline.getAi1(), cmdline.getAi1Level()));
        config.setPlayerProfile(1, AiProfile.create(cmdline.getAi2(), cmdline.getAi2Level()));
        setPlayerDeck(cmdline.getDeck1(), config.getPlayerConfig(0));
        setPlayerDeck(cmdline.getDeck2(), config.getPlayerConfig(1));
        config.setNrOfGames(cmdline.getGames());
        config.setStartLife(cmdline.getLife());

        System.out.println("P1 : " + config.getPlayerProfile(0).getPlayerLabel());
        System.out.println("     " + config.getPlayerConfig(0).getDeck().getQualifiedName());
        System.out.println("P2 : " + config.getPlayerProfile(1).getPlayerLabel());
        System.out.println("     " + config.getPlayerConfig(1).getDeck().getQualifiedName());
        System.out.println("Threads : " + MagicAI.getMaxThreads());

        ScreenController.getFrame().newDuel(config);
    }

    private static void startUI(CommandLineArgs cmdline) {

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
            try {
                runAivsAiGame(cmdline);
            } catch (InvalidDeckException ex) {
                System.err.println(ex);
            } catch (RuntimeException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
            return;
        }

        // normal UI startup.
        ScreenController.showStartScreen();

    }
}
