package magic.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.MagicRandom;

//AI that plays randomly
public class RandomAI implements MagicAI {
    
    private final boolean LOGGING;
   
    public RandomAI() {
        this(false);
    }

    public RandomAI(boolean printLog) {
        LOGGING = printLog;
    }
    
    private void log(final String message) {
        if (LOGGING) {
            System.out.println(message);
        }
    }
    
    public synchronized Object[] findNextEventChoiceResults(
            final MagicGame game, 
            final MagicPlayer scorePlayer) {

        //get a list of choices
        MagicGame choiceGame = new MagicGame(game, scorePlayer);
        final MagicEvent event=choiceGame.getNextEvent();
		final List<Object[]> choices=event.getArtificialChoiceResults(choiceGame);
        choiceGame = null;
        
        final int size = choices.size();
        final String info = "RandomAI " + scorePlayer.getIndex() + " (" + scorePlayer.getLife() + ")";
      
        if (size == 0) {
            log(info + " NO CHOICE");
            return null;
        } else { //size >= 1
            //build a list of artificial choice results
            final List<ArtificialChoiceResults> achoices=new ArrayList<ArtificialChoiceResults>();
            for (final Object choice[] : choices) {
                achoices.add(new ArtificialChoiceResults(choice));
            }
		
            // Select a random artificial choice result
            final int idx = MagicRandom.nextInt(size);
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
