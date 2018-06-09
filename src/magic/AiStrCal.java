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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Collections;
import java.util.ArrayList;

public class AiStrCal {

    private static int games = 10;
    private static int repeat = 1;
    private static int life = 20;
    private static int seed;
    private static String profile = "**";
    private static List<String> deckPool;
    private static String[] deck = {"", ""};
    private static MagicAIImpl[] ai = {MagicAIImpl.MMAB, MagicAIImpl.MMAB};
    private static int[] str = {6, 6};
    private static int winTotal = 0;
    private static int gamesTotal = 0;
    private static boolean verifyFlip = false;

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
            } else if ("--deckpool".equals(curr)) {
                try (Stream<Path> paths = Files.walk(Paths.get(next))) {
                    deckPool = paths
                        .filter(Files::isRegularFile)
                        .map(Path::toString)
                        .collect(Collectors.toList());
                }catch(Exception e){
                    System.err.println("Error reading from "+ next);

                }
            } else if ("--profile".equals(curr)) {
                profile = next;
            } else if ("--verify".equals(curr)) {
                verifyFlip = true;
            } else if ("--ai1".equals(curr)) {
                try { //parse CLI option
                    ai[0] = MagicAIImpl.valueOf(next);
//                    ai[0].getAI().setMaxThreads(2);
                } catch (final IllegalArgumentException ex) {
                    System.err.println("Error: " + next + " is not valid AI");
                    validArgs = false;
                }
            } else if ("--ai2".equals(curr)) {
                try { //parse CLI option
                    ai[1] = MagicAIImpl.valueOf(next);
//                    ai[1].getAI().setMaxThreads(2);
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
            if(deckPool != null){
              System.err.println("Using decklists from pool");
            } else if (deck[i].length() == 0) {
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
            System.err.println("--deckpool <dir>                 (directory from where to read deck lists)");
            System.err.println("--verify   true                  (verify the hands of players are equal after flip)");
            System.exit(1);
        }

        MagicSystem.initialize(new ProgressReporter());
        ArrayList<String> reversePool = new ArrayList<String>(deckPool);
        Collections.reverse(reversePool);
        for (int i = 0; i < repeat; i++) {
            if(deckPool == null) {
                runDuel();
            }else{
                for(String deck1: reversePool){
                    for(String deck2: deckPool){
                        deck[0] = deck1;
                        deck[1] = deck2;
                        runDuel();
                    }
                }
            }
        }
    }

    private static void compareHands(String expected, String actual){
        if(!expected.equals(actual)){
            System.err.println("Hands to not match");
            System.err.println(expected);
            System.err.println("----");
            System.err.println(actual);
        }
    }

    private static void runDuel() {
        final MagicDuel testDuel = setupDuel();

        System.out.println(
                padDeckLeft("deck1") +
                "\t"+padAILeft("ai1") +
                "\tstr1" +
                "\t"+padDeckLeft("deck2") +
                "\t"+padAILeft("ai2") +
                "\tstr2" +
                "\tgames" +
                "\td1win"+
                "\td1lose"
        );

        int played = 0;
        int p1Seed = 1234;
        int p2Seed = 4321;

        String hand1 ="";
        String hand2 ="";
        while (testDuel.getGamesPlayed() < testDuel.getGamesTotal()) {

            if(played % 2 == 0){
                p1Seed = MagicRandom.nextRNGInt();
                p2Seed = MagicRandom.nextRNGInt();
            }else{
                int tmp = p1Seed;
                p1Seed = p2Seed;
                p2Seed = tmp;
            }
            final MagicGame game=testDuel.nextGame(p1Seed, p2Seed, played != 0);
            if(verifyFlip) {
                if (played % 2 == 0) {
                    hand1 = game.getPlayer(0).getHand().toString();
                    hand2 = game.getPlayer(1).getHand().toString();
                } else {
                    compareHands(hand2, game.getPlayer(0).getHand().toString());
                    compareHands(hand1, game.getPlayer(1).getHand().toString());
                }
            }
            game.setArtificial(true);


            //maximum duration of a game is 60 minutes
            final HeadlessGameController controller = new HeadlessGameController(game, 3600000);

            controller.runGame();

            if (testDuel.getGamesPlayed() > played) {
                System.err.println(
                        padDeckLeft(deckName(deck[played % 2])) + "\t" +
                        padAILeft(ai[0].toString()) + "\t" +
                        str[0] + "\t" +
                        padDeckLeft(deckName(deck[(played+1) % 2])) + "\t" +
                        padAILeft(ai[1].toString()) + "\t" +
                        str[1] + "\t" +
                        testDuel.getGamesTotal() + "\t" +
                        testDuel.getGamesWon() + "\t" +
                        (testDuel.getGamesPlayed() - testDuel.getGamesWon())
                );
                played = testDuel.getGamesPlayed();
            }
        }
        winTotal += testDuel.getGamesWon();
        gamesTotal += testDuel.getGamesTotal();
        System.out.println(
                padDeckLeft("all")+ "\t" +
                padAILeft(ai[0].toString()) + "\t" +
                str[0] + "\t" +
                padDeckLeft("all") + "\t" +
                padAILeft(ai[1].toString()) + "\t" +
                str[1] + "\t" +
                gamesTotal + "\t" +
                winTotal + "\t" +
                (gamesTotal - winTotal) + "\t" +
                (100*winTotal/gamesTotal)+"%"
        );
    }
    private static String deckName(String path){
        String[] bits = path.split("/");
        return bits[bits.length-1];
    }

    public static String padAILeft(String s) {
        int n = 25;
        return String.format("%1$" + n + "s", s.substring(0, Math.min(s.length(), n)));
    }

    public static String padDeckLeft(String s) {
        int n = 25;
        return String.format("%1$" + n + "s", s.substring(0, Math.min(s.length(), n)));
    }
}
