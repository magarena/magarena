package magic.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import magic.model.MagicGame;
import magic.model.MagicRandom;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;

public class VegasAI implements MagicAI {

	private static final int THREADS=Runtime.getRuntime().availableProcessors();
	private static final int SIMULATIONS=120;
	
	@Override
	public Object[] findNextEventChoiceResults(final MagicGame sourceGame,final MagicPlayer scorePlayer) {

		final MagicGame choiceGame=new MagicGame(sourceGame,scorePlayer);
		choiceGame.setKnownCards();
		final MagicEvent event=choiceGame.getNextEvent();
		final List<Object[]> choiceResultsList=event.getArtificialChoiceResults(choiceGame);
		
		// No choices
		final int size=choiceResultsList.size();
		if (size==0) {
			return null;
		}		
		
		// Single choice		
		if (size==1) {
			return sourceGame.map(choiceResultsList.get(0));
		}
		
		// Multiple choices
		final BlockingQueue<Runnable> queue=new ArrayBlockingQueue<Runnable>(size*THREADS);
		final ThreadPoolExecutor executor=new ThreadPoolExecutor(THREADS,THREADS,2,TimeUnit.MINUTES,queue);
		final List<VegasScore> scores=new ArrayList<VegasScore>();
		final int simulations=(sourceGame.getArtificialLevel()*SIMULATIONS)/THREADS;
		for (final Object[] choiceResults : choiceResultsList) {
		
			final VegasScore score=new VegasScore(choiceResults);
			scores.add(score);
			for (int count=THREADS;count>0;count--) {
				
				final VegasWorker worker=new VegasWorker(
                        choiceGame,
                        choiceGame.getScorePlayer(),
                        score,
                        new Random(MagicRandom.nextInt(1000000)),
                        simulations);
				executor.execute(worker);
			}
		}
		executor.shutdown();
		try {
			executor.awaitTermination(30,TimeUnit.SECONDS);
		} catch (final InterruptedException ex) {}
		
		// Return best choice
		int bestIndex=0;
		int bestScore=scores.get(0).getScore();
		for (int index=1;index<size;index++) {
			
			final int score=scores.get(index).getScore();
			if (score>bestScore) {
				bestScore=score;
				bestIndex=index;
			}
		}
		return sourceGame.map(scores.get(bestIndex).getChoiceResults());
	}
}
