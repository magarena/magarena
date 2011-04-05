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
import magic.ai.MagicAIImpl;

public class DeckStrCal {
   
    public static void main(final String[] args) {
        int games = 10;
        int strength = 6;
        String deck1 = "";
        String deck2 = "";
        MagicAIImpl ai1 = MagicAIImpl.DEFAULT;
        MagicAIImpl ai2 = MagicAIImpl.DEFAULT;

        // Command line parsing.
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
                ai1 = MagicAIImpl.valueOf(next);
            } else if (curr.equals("--ai2")) {
                ai2 = MagicAIImpl.valueOf(next);
            }
            else {
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
        
        // Set the AI
        testTournament.setAIs(ai1.getAI(), ai2.getAI());

        // Set the deck.
        DeckUtils.loadDeck(deck1, testTournament.getPlayer(0)); 
        DeckUtils.loadDeck(deck2, testTournament.getPlayer(1));

        
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
            final MagicGame game=testTournament.nextGame();
            final GameController controller=new GameController(null,game);
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