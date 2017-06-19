package magic;

import java.io.File;
import magic.ai.MagicAIImpl;
import magic.data.DeckGenerators;
import magic.data.DuelConfig;
import magic.exception.handler.ConsoleExceptionHandler;
import magic.headless.HeadlessGameController;
import magic.model.DuelPlayerConfig;
import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicRandom;
import magic.model.player.AiProfile;
import magic.utility.DeckUtils;
import magic.utility.MagicSystem;
import magic.utility.ProgressReporter;

public class DeckStrCal {

    private static int games = 10;
    private static int repeat = 1;
    private static int life = 20;
    private static int seed;
    private static String profile = "**";
    private static String[] deck = {"", ""};
    private static MagicAIImpl[] ai = {MagicAIImpl.MMAB, MagicAIImpl.MMAB};
    private static int[] str = {6, 6};

    // Command line parsing.
    private static boolean parseArguments(final String[] args) {
        boolean validArgs = true;
        for (int i = 0; i < args.length; i += 2) {
            final String curr = args[i];
            final String next = args[i+1];
            if ("--games".equals(curr)) {
                try { //parse CLI option
                    games = Integer.parseInt(next);
                } catch (final NumberFormatException ex) {
                    System.err.println("ERROR! number of games not an integer");
                    validArgs = false;
                }
            } else if ("--str1".equals(curr)) {
                try { //parse CLI option
                    str[0] = Integer.parseInt(next);
                } catch (final NumberFormatException ex) {
                    System.err.println("ERROR! AI strength not an integer");
                    validArgs = false;
                }
            } else if ("--str2".equals(curr)) {
                try { //parse CLI option
                    str[1] = Integer.parseInt(next);
                } catch (final NumberFormatException ex) {
                    System.err.println("ERROR! AI strength not an integer");
                    validArgs = false;
                }
            } else if ("--deck1".equals(curr)) {
                deck[0] = next;
            } else if ("--deck2".equals(curr)) {
                deck[1] = next;
            } else if ("--profile".equals(curr)) {
                profile = next;
            } else if ("--ai1".equals(curr)) {
                try { //parse CLI option
                    ai[0] = MagicAIImpl.valueOf(next);
                } catch (final IllegalArgumentException ex) {
                    System.err.println("Error: " + next + " is not valid AI");
                    validArgs = false;
                }
            } else if ("--ai2".equals(curr)) {
                try { //parse CLI option
                    ai[1] = MagicAIImpl.valueOf(next);
                } catch (final IllegalArgumentException ex) {
                    System.err.println("Error: " + next + " is not valid AI");
                    validArgs = false;
                }
            } else if ("--life".equals(curr)) {
                try { //parse CLI option
                    life = Integer.parseInt(next);
                } catch (final NumberFormatException ex) {
                    System.err.println("ERROR! starting life is not an integer");
                    validArgs = false;
                }
            } else if ("--repeat".equals(curr)) {
                try { //parse CLI option
                    repeat = Integer.parseInt(next);
                } catch (final NumberFormatException ex) {
                    System.err.println("ERROR! repeat is not an integer");
                    validArgs = false;
                }
            } else if ("--seed".equals(curr)) {
                try { //parse CLI option
                    seed = Integer.parseInt(next);
                } catch (final NumberFormatException ex) {
                    System.err.println("ERROR! seed is not an integer");
                    validArgs = false;
                }
            } else {
                System.err.println("Error: unknown option " + curr);
                validArgs = false;
            }
        }

        for (int i = 0; i < 2; i++) {
            if (deck[i].length() == 0) {
                System.err.println("Using profile " + profile + " to generate deck " + (i+1));
            } else if (!(new File(deck[i])).exists()) {
                System.err.println("Error: file " + deck[i] + " does not exist");
                validArgs = false;
            }
        }

        return validArgs;
    }

    private static MagicDuel setupDuel() {
        // Set the random seed
        if (seed != 0) {
            MagicRandom.setRNGState(seed);
            seed = MagicRandom.nextRNGInt() + 1;
        }

        // Set number of games.
        final DuelConfig config=new DuelConfig();
        config.setNrOfGames(games);
        config.setStartLife(life);

        // Set difficulty.
        final MagicDuel testDuel=new MagicDuel(config);
        testDuel.initialize();

        // Create players
        final DuelPlayerConfig[] players = new DuelPlayerConfig[2];
        for (int i = 0; i < 2; i++) {
            players[i] = new DuelPlayerConfig(
                AiProfile.create(ai[i], str[i]),
                MagicDeckProfile.getDeckProfile(profile)
            );
        }
        testDuel.setPlayers(players);

        // Set the deck.
        for (int i = 0; i < 2; i++) {
            if (deck[i].length() > 0) {
                DeckUtils.loadAndSetPlayerDeck(deck[i], players[i]);
            } else {
                DeckGenerators.setRandomDeck(players[i]);
            }
        }

        return testDuel;
    }

    public static void main(final String[] args) {

        Thread.setDefaultUncaughtExceptionHandler(new ConsoleExceptionHandler());

        if (!parseArguments(args)) {
            System.err.println("Usage: java -cp <path to Magarena.jar/exe> magic.DeckStrCal --deck1 <.dec file> --deck2 <.dec file> [options]");
            System.err.println("Options:");
            System.err.println("--ai1      [MMAB|MMABC|MCTS|RND] (AI for player 1, default MMAB)");
            System.err.println("--ai2      [MMAB|MMABC|MCTS|RND] (AI for player 2, default MMAB)");
            System.err.println("--strength <1-8>                 (level of AI, default 6)");
            System.err.println("--games    <1-*>                 (number of games to play, default 10)");
            System.exit(1);
        }

        MagicSystem.initialize(new ProgressReporter());

        for (int i = 0; i < repeat; i++) {
            runDuel();
        }
    }

    private static void runDuel() {
        final MagicDuel testDuel = setupDuel();

        System.out.println(
                 "#deck1" +
                "\tai1" +
                "\tstr1" +
                "\tdeck2" +
                "\tai2" +
                "\tstr2" +
                "\tgames" +
                "\td1win"+
                "\td1lose"
        );

        int played = 0;
        while (testDuel.getGamesPlayed() < testDuel.getGamesTotal()) {
            final MagicGame game=testDuel.nextGame();
            game.setArtificial(true);

            //maximum duration of a game is 60 minutes
            final HeadlessGameController controller = new HeadlessGameController(game, 3600000);

            controller.runGame();
            if (testDuel.getGamesPlayed() > played) {
                System.err.println(
                        deck[0] + "\t" +
                        ai[0] + "\t" +
                        str[0] + "\t" +
                        deck[1] + "\t" +
                        ai[1] + "\t" +
                        str[1] + "\t" +
                        testDuel.getGamesTotal() + "\t" +
                        testDuel.getGamesWon() + "\t" +
                        (testDuel.getGamesPlayed() - testDuel.getGamesWon())
                );
                played = testDuel.getGamesPlayed();
            }
        }
        System.out.println(
                deck[0] + "\t" +
                ai[0] + "\t" +
                str[0] + "\t" +
                deck[1] + "\t" +
                ai[1] + "\t" +
                str[1] + "\t" +
                testDuel.getGamesTotal() + "\t" +
                testDuel.getGamesWon() + "\t" +
                (testDuel.getGamesPlayed() - testDuel.getGamesWon())
        );
    }
}
