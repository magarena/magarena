package magic;

import java.util.Collections;
import magic.ai.MagicAI;
import magic.data.DuelConfig;
import magic.exception.handler.ConsoleExceptionHandler;
import magic.headless.HeadlessGameController;
import magic.model.MagicDeck;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicRandom;
import magic.model.player.AiProfile;
import magic.ui.WikiPage;
import magic.utility.MagicSystem;
import magic.utility.ProgressReporter;

public class HeadlessAiGame {

    private static final String H1 = String.join("", Collections.nCopies(60, "="));
    private static final String H2 = String.join("", Collections.nCopies(60, "-"));

    private static int seed;

    private static MagicDuel setupDuel(CommandLineArgs args) {

        // Set the random seed
        if (seed != 0) {
            MagicRandom.setRNGState(seed);
            seed = MagicRandom.nextRNGInt() + 1;
        }

        // Set number of games.
        final DuelConfig config=new DuelConfig();
        config.setNrOfGames(args.getGames());
        config.setStartLife(args.getLife());

        // Create players
        config.setPlayerProfile(0, AiProfile.create(args.getAi1(), args.getAi1Level()));
        config.setPlayerProfile(1, AiProfile.create(args.getAi2(), args.getAi2Level()));
        MagicMain.setPlayerDeck(args.getDeck1(), config.getPlayerConfig(0));
        MagicMain.setPlayerDeck(args.getDeck2(), config.getPlayerConfig(1));

        // Set difficulty.
        final MagicDuel testDuel=new MagicDuel(config);
        testDuel.initialize();

        return testDuel;
    }

    private static void parseCommandLine(CommandLineArgs cmdline) {

        if (cmdline.showHelp()) {
            System.err.println("--help specified - opening wiki page and exit...\n" + WikiPage.COMMAND_LINE.getUrl());
            WikiPage.show(WikiPage.COMMAND_LINE);
            System.exit(0);
        }

        if (cmdline.showFPS() || cmdline.getFPS() > 0) {
            System.err.println("--fps not supported.");
            System.exit(0);
        }

        MagicAI.setMaxThreads(cmdline.getMaxThreads());
        MagicSystem.setIsDevMode(cmdline.isDevMode());
    }

    private static void parseCommandLineAndRun(String[] args) {

        CommandLineArgs cmdline = new CommandLineArgs(args);
        parseCommandLine(cmdline);

        MagicSystem.initialize(new ProgressReporter());

        System.out.println();
        System.out.println("=================== AI vs AI (headless) ====================");
        System.out.println("Threads : " + MagicAI.getMaxThreads());
        System.out.println("Life : " + cmdline.getLife());
        System.out.println("Duels : " + cmdline.getDuels());
        System.out.println("Games : " + cmdline.getGames());

        // run getGames() games getDuels() times.
        for (int i = 0; i < cmdline.getDuels(); i++) {
            runDuel(cmdline, i+1);
        }
    }

    public static void main(final String[] args) {

        Thread.setDefaultUncaughtExceptionHandler(new ConsoleExceptionHandler());

        try {
            parseCommandLineAndRun(args);
        } catch (RuntimeException ex) {
            System.err.println(ex);
            System.err.println();
            System.err.println("Usage: java -cp <path to Magarena.jar/exe> magic.HeadlessAiGame --help");
            System.err.println("- to display wiki page in browser");
        }
    }

    private static void runDuel(CommandLineArgs args, int duelNum) {

        final MagicDuel duel = setupDuel(args);

        if (duelNum == 1) {
            AiProfile p1 = (AiProfile) duel.getPlayer(0).getProfile();
            AiProfile p2 = (AiProfile) duel.getPlayer(1).getProfile();
            System.out.printf("P1 : %s [%d]\n", p1.getAiType(), p1.getAiLevel());
            System.out.printf("P2 : %s [%d]\n", p2.getAiType(), p2.getAiLevel());
        }

        MagicDeck d1 = duel.getPlayer(0).getDeck();
        MagicDeck d2 = duel.getPlayer(1).getDeck();

        System.out.println(H1);
        System.out.printf("Duel %d of %d\n", duelNum, args.getDuels());
        System.out.println("D1 : " + d1.getQualifiedName());
        System.out.println("D2 : " + d2.getQualifiedName());
        System.out.println(H2);
        System.out.println("Game  Won  D1  D2  Duration");

        int played = 0;
        while (duel.getGamesPlayed() < duel.getGamesTotal()) {

            final MagicGame game=duel.nextGame();
            game.setArtificial(true);

            //maximum duration of a game is 10 minutes
            final HeadlessGameController controller = new HeadlessGameController(
                game, 600000
            );

            final long start_time = System.currentTimeMillis();
            controller.runGame();
            final double duration = (double)(System.currentTimeMillis() - start_time) / 1000;

            played++;

            System.out.printf("%d     %s   %d   %d   %.2f\n",
                played,
                game.getWinner().getConfig().getDeck().equals(d1) ? "D1" : "D2",
                duel.getGamesWon(),
                duel.getGamesPlayed() - duel.getGamesWon(),
                duration
            );
        }
    }
}
