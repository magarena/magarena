package magic.ai;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicRandom;
import magic.model.event.MagicEvent;

import java.util.ArrayList;
import java.util.List;

//AI that plays randomly
public class RandomAI implements MagicAI {
    
    private final boolean LOGGING;
   
    public RandomAI() {
        this(false);
    }

    public RandomAI(final boolean log) {
        LOGGING = log || (System.getProperty("debug") != null);
    }
    
    private void log(final String message) {
        if (LOGGING) {
            System.err.println(message);
        }
    }
    
    public Object[] findNextEventChoiceResults(final MagicGame game, final MagicPlayer scorePlayer) {
        //get a list of choices
        MagicGame choiceGame = new MagicGame(game, scorePlayer);
        final MagicEvent event=choiceGame.getNextEvent();
        final List<Object[]> choices=event.getArtificialChoiceResults(choiceGame);
        choiceGame = null;
        
        final int size = choices.size();
        final String info = "RandomAI " + scorePlayer.getIndex() + " (" + scorePlayer.getLife() + ")";
      
        if (size == 0) {
            throw new RuntimeException("No choice results");
        }

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
            //log(info + " " + selected); 
        }
        return game.map(selected.choiceResults);
    }
}
