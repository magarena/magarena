package magic.ai;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;

import java.util.List;
import java.util.Random;

public class VegasWorker implements Runnable {

    private static final int MAIN_PHASES=6;
    
    private final MagicGame sourceGame;
    private final MagicPlayer scorePlayer;
    private final VegasScore score;
    private final Object[] choiceResults;
    private final Random random;
    private final long endTime;
    
    VegasWorker(final MagicGame sourceGame,final MagicPlayer scorePlayer,final VegasScore score,final Random random,final long endTime) {
        this.sourceGame=sourceGame;
        this.scorePlayer=scorePlayer;
        this.score=score;
        this.choiceResults=score.getChoiceResults();
        this.random=random;
        this.endTime=endTime;
    }

    /** Play game until number of main phases are completed or until the game is finished. */
    private void runGame(final MagicGame game) {
        while (!game.isFinished()) {
            if (!game.hasNextEvent()) {
                game.executePhase();
                continue;
            }

            final MagicEvent event=game.getNextEvent();
            
            if (!event.hasChoice()) {
                game.executeNextEvent(MagicEvent.NO_CHOICE_RESULTS);
                continue;
            }
            
            final List<Object[]> choiceResultsList=event.getArtificialChoiceResults(game);
            final int nrOfChoices=choiceResultsList.size();

            assert nrOfChoices != 0 : "ERROR: no choices available for VegasWorker";
            
            game.executeNextEvent(choiceResultsList.get(random.nextInt(nrOfChoices)));
        }        
    }
    
    @Override
    public void run() {
        while (System.nanoTime() < endTime) {
            final MagicGame game=new MagicGame(sourceGame,scorePlayer);    
            game.setMainPhases(MAIN_PHASES);
            game.executeNextEvent(game.map(choiceResults));
            runGame(game);
            score.incrementScore(game.getScore());
        }
    }    
}
