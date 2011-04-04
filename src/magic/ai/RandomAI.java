package magic.ai;

import java.util.Random;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import magic.model.MagicGame;
import magic.model.phase.MagicPhase;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;

//AI that plays randomly
public class RandomAI implements MagicAI {
    
    private static final boolean LOGGING=true;
    private static final Random RNG = new Random();
    
    private static void log(final String message) {
        if (LOGGING) {
            System.out.println(message);
        }
    }
    
    public synchronized Object[] findNextEventChoiceResults(
            final MagicGame game, 
            final MagicPlayer scorePlayer) {

        long time=System.currentTimeMillis();

        //get a list of choices
        MagicGame choiceGame = new MagicGame(game, scorePlayer);
        final MagicEvent event=choiceGame.getNextEvent();
		final List<Object[]> choices=event.getArtificialChoiceResults(choiceGame);
        choiceGame = null;
        
        final int size = choices.size();
        final String info = "RandomAI " + scorePlayer.getIndex() + " (" + scorePlayer.getLife() + ")";
      
        if (size == 0) {
            log(info + " NO CHOICE");
            System.exit(1);
            return null;
        } else { //size >= 1
            //build a list of artificial choice results
            final List<ArtificialChoiceResults> achoices=new ArrayList<ArtificialChoiceResults>();
            for (final Object choice[] : choices) {
                achoices.add(new ArtificialChoiceResults(choice));
            }
		
            // Select a random artificial choice result
            final int idx = RNG.nextInt(size);
		    final ArtificialChoiceResults selected=achoices.get(idx);
            if (size >= 2) {
                log(info); 
                for (final ArtificialChoiceResults achoice : achoices) {
                    log((achoice==selected?"* ":"  ")+achoice);
                }
            } else {
                log(info + " " + selected); 
            }
            return game.map(selected.choiceResults);
        }
    }
}
