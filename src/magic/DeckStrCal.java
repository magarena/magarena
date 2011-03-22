package magic;

import java.io.File;

import magic.data.CardDefinitions;
import magic.data.CardEventDefinitions;
import magic.data.CubeDefinitions;
import magic.data.DeckUtils;
import magic.data.KeywordDefinitions;
import magic.data.LocalVariableDefinitions;
import magic.data.ManaActivationDefinitions;
import magic.data.PermanentActivationDefinitions;
import magic.data.TournamentConfig;
import magic.data.TriggerDefinitions;
import magic.model.MagicGame;
import magic.model.MagicTournament;
import magic.model.variable.MagicStaticLocalVariable;
import magic.ui.GameController;

public class DeckStrCal {
	
	public static void main(final String[] args) {
        int games = 10;
        int strength = 6;
        String deck1 = "";
        String deck2 = "";

        // Command line parsing.
        for (int i = 0; i < args.length; i += 2) {
        	
            if (args[i].equals("--games")) {
                try {
                    games = Integer.parseInt(args[i+1]);
                } catch (final Exception ex) {
                    System.err.println("Error: number of games not an integer");
                    ex.printStackTrace();
                    System.exit(1);
                }
            } else if (args[i].equals("--strength")) {
                try {
                    strength = Integer.parseInt(args[i+1]);
                } catch (final Exception ex) {
                    System.err.println("Error: AI strength not an integer");
                    ex.printStackTrace();
                    System.exit(1);
                }
            } else if (args[i].equals("--deck1")) {
                deck1 = args[i+1];
            } else if (args[i].equals("--deck2")) {
                deck2 = args[i+1];
            } else {
                System.err.println("Error: unknown option");
                System.exit(1);
            }
        }
       
        if (!(new File(deck1)).exists()) {
            System.err.println("Error: file " + deck1 + " does not exist");
            System.exit(1);
        }
        
        if (!(new File(deck2)).exists()) {
            System.err.println("Error: file " + deck2 + " does not exist");
            System.exit(1);
        }

		// Load cards and cubes.
        try {
			CardDefinitions.getInstance().loadCardDefinitions();
			CubeDefinitions.getInstance().loadCubeDefinitions();
			KeywordDefinitions.getInstance().loadKeywordDefinitions();
			TriggerDefinitions.addTriggers();
			LocalVariableDefinitions.addLocalVariables();
			ManaActivationDefinitions.addManaActivations();
			PermanentActivationDefinitions.addPermanentActivations();
			CardEventDefinitions.setCardEvents();
			MagicStaticLocalVariable.initializeCardDefinitions();
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
      
        // Set the deck.
        DeckUtils.loadDeck(deck1, testTournament.getPlayer(0)); 
        DeckUtils.loadDeck(deck2, testTournament.getPlayer(1));
        
        System.out.println(
                 "#deck1" +
                "\tdeck2" +
                "\tstrength" +
                "\tgames" +
                "\td1win"+
                "\td1lose" 
        ); 

        int played = 0;
        while (!testTournament.isFinished()) {
            final MagicGame game=testTournament.nextGame();
            final GameController controller=new GameController(null,game);
            controller.runGame();
            if (testTournament.getGamesPlayed() > played) {
                System.out.println(
                        deck1 + "\t" + 
                        deck2 + "\t" + 
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