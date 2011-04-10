package magic.ai;

import java.util.List;
import java.util.Random;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;

public class VegasWorker implements Runnable {

	private static final int MAIN_PHASES=6;
	
	private final MagicGame sourceGame;
	private final MagicPlayer scorePlayer;
	private final VegasScore score;
	private final Object choiceResults[];
	private final Random random;
	private final int simulations;
	
	public VegasWorker(final MagicGame sourceGame,final MagicPlayer scorePlayer,final VegasScore score,final Random random,final int simulations) {

		this.sourceGame=sourceGame;
		this.scorePlayer=scorePlayer;
		this.score=score;
		this.choiceResults=score.getChoiceResults();
		this.random=random;
		this.simulations=simulations;
	}

	/** Play game until number of main phases are completed or until the game is finished. */
	private boolean runGame(final MagicGame game) {

		while (!game.isFinished()) {
			
			if (game.hasNextEvent()) {
				final MagicEvent event=game.getNextEvent();
				if (event.hasChoice()) {					
					final List<Object[]> choiceResultsList=event.getArtificialChoiceResults(game);
					final int nrOfChoices=choiceResultsList.size();
					if (nrOfChoices==0) {
						// No choices, game is invalid.
						return false;
					} else if (nrOfChoices==1) {
						game.executeNextEvent(choiceResultsList.get(0));
					} else {
						game.executeNextEvent(choiceResultsList.get(random.nextInt(nrOfChoices)));
					}
				} else {
					game.executeNextEvent(MagicEvent.NO_CHOICE_RESULTS);
				}
			} else {
				game.getPhase().executePhase(game);
			}
		}		
		return true;
	}
	
	@Override
	public void run() {

		for (int count=simulations;count>0;count--) {

			final MagicGame game=new MagicGame(sourceGame,scorePlayer);	
			game.setMainPhases(MAIN_PHASES);
			game.executeNextEvent(game.map(choiceResults));
			if (runGame(game)) {
				score.incrementScore(game.getScore());
			}
		}
	}	
}