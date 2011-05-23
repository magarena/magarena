package magic;

import java.io.File;

import magic.ai.MagicAI;
import magic.ai.MagicAIImpl;
import magic.data.DeckUtils;
import magic.data.TournamentConfig;
import magic.model.MagicGame;
import magic.model.MagicTournament;
import magic.ui.GameController;

public class DeckStrCal {
        
    private static int games = 10;
    private static int strength = 6;
    private static String deck1 = "";
    private static String deck2 = "";
    private static MagicAIImpl ai1 = MagicAIImpl.DEFAULT;
    private static MagicAIImpl ai2 = MagicAIImpl.DEFAULT;

    // Command line parsing.
    private static boolean parseArguments(final String[] args) {
        boolean validArgs = true;
        for (int i = 0; i < args.length; i += 2) {
            String curr = args[i];
            String next = args[i+1];
            if (curr.equals("--games")) {
                try {
                    games = Integer.parseInt(next);
                } catch (final Exception ex) {
                    System.err.println("Error: number of games not an integer");
                    ex.printStackTrace();
                    System.exit(1);
                }
            } else if (curr.equals("--strength")) {
                try {
                    strength = Integer.parseInt(next);
                } catch (final Exception ex) {
                    System.err.println("Error: AI strength not an integer");
                    ex.printStackTrace();
                    System.exit(1);
                }
            } else if (curr.equals("--deck1")) {
                deck1 = next;
            } else if (curr.equals("--deck2")) {
                deck2 = next;
            } else if (curr.equals("--ai1")) {
                try {
                    ai1 = MagicAIImpl.valueOf(next);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error: " + next + " is not valid AI");
                    validArgs = false;
                }
            } else if (curr.equals("--ai2")) {
                try {
                    ai2 = MagicAIImpl.valueOf(next);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error: " + next + " is not valid AI");
                    validArgs = false;
                }
            }
            else {
                System.err.println("Error: unknown option " + curr);
                validArgs = false;
            }
        }
      
        if (deck1.length() == 0) {
            System.err.println("Error: no file specified for deck 1");
            validArgs = false;
        } else if (!(new File(deck1)).exists()) {
            System.err.println("Error: file " + deck1 + " does not exist");
            validArgs = false;
        }
        
        if (deck2.length() == 0) {
            System.err.println("Error: no file specified for deck 2");
            validArgs = false;
        } else if (!(new File(deck2)).exists()) {
            System.err.println("Error: file " + deck2 + " does not exist");
            validArgs = false;
        }

        return validArgs;
    }

    private static MagicTournament setupTournament() {
        // Load cards and cubes.
        try {
        	MagicMain.initializeEngine();
        } catch (final Exception ex) {
            System.err.println("Error: unable to initialize game engine");
            ex.printStackTrace();
            System.exit(1);
        }

        // Set number of games.
        final TournamentConfig config=new TournamentConfig();
        config.setNrOfGames(games);

        // Set difficulty.
        final MagicTournament testTournament=new MagicTournament(config);
        testTournament.initialize();
        testTournament.setDifficulty(strength);
        
        // Set the AI
        testTournament.setAIs(new MagicAI[]{ai1.getAI(), ai2.getAI()});

        // Set the deck.
        DeckUtils.loadDeck(deck1, testTournament.getPlayer(0)); 
        DeckUtils.loadDeck(deck2, testTournament.getPlayer(1));

        return testTournament;
    }
   
    public static void main(final String[] args) {
        if (!parseArguments(args)) {
            System.err.println("Usage: java -jar <path to Magarena.jar/exe> magic.DeckStrCal --deck1 <.dec file> --deck2 <.dec file> [options]");
            System.err.println("Options:");
            System.err.println("--games <1-*> [number of games to play, default 10]");
            System.err.println("--strength <1-8> [strength of default AI, default 6]");
            System.exit(1);
        }

        MagicTournament testTournament = setupTournament();
        
        System.out.println(
                 "#deck1" +
                "\tai1" +
                "\tdeck2" +
                "\tai2" +
                "\tstrength" +
                "\tgames" +
                "\td1win"+
                "\td1lose" 
        ); 

        int played = 0;
        while (!testTournament.isFinished()) {
            final MagicGame game=testTournament.nextGame(false);
            final GameController controller=new GameController(null,game);
            controller.setMaxTestGameDuration(Long.MAX_VALUE);
            controller.runGame();
            if (testTournament.getGamesPlayed() > played) {
                System.out.println(
                        deck1 + "\t" +
                        ai1 + "\t" +
                        deck2 + "\t" + 
                        ai2 + "\t" +
                        strength + "\t" +
                        testTournament.getGamesTotal() + "\t" +
                        testTournament.getGamesWon() + "\t" +
                        (testTournament.getGamesPlayed() - testTournament.getGamesWon())
                ); 
                played = testTournament.getGamesPlayed();
            }
        }
    }
}
