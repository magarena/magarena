package magic.ai;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MMAB implements MagicAI {
	
	private static final int INITIAL_MAX_DEPTH=110;
	private static final int INITIAL_MAX_GAMES=10000;
	private static final int MAX_DEPTH=120;
	private static final int MAX_GAMES=12000;

    // maximum of 4 artificial worker threads.
	private final int THREADS = Math.min(4,Runtime.getRuntime().availableProcessors());
	
    // list of workers
    private final LinkedList<ArtificialWorker> workers = new LinkedList<ArtificialWorker>();
    
    private final boolean LOGGING;
    private final boolean CHEAT;

    private int processingLeft;
	private ArtificialPruneScore pruneScore;

    MMAB() {
        //default: no logging, no cheats
        this(false, false);
    }
    
    MMAB(final boolean log, final boolean cheat) {
        LOGGING = log || (System.getProperty("debug") != null);
        CHEAT = cheat;
    }
	
	private void log(final String message) {
		if (LOGGING) {
			System.err.println(message);
		}
	}
	
	private static ArtificialPruneScore createPruneScore() {
		return new ArtificialMultiPruneScore();
	}
    
    public synchronized Object[] findNextEventChoiceResults(final MagicGame sourceGame, final MagicPlayer scorePlayer) {
		final long start_time = System.currentTimeMillis();

		// copying the game is necessary because for some choices game scores might be calculated, 
        // find all possible choice results.
		MagicGame choiceGame = new MagicGame(sourceGame,scorePlayer);
		final MagicEvent event = choiceGame.getNextEvent();
		final List<Object[]> choices = event.getArtificialChoiceResults(choiceGame);
		final int size = choices.size();
		choiceGame = null;
		
		assert size != 0 : "ERROR: no choices available for MMAB";
		
		// single choice result.
		if (size == 1) {
			return sourceGame.map(choices.get(0));
		}
		
		// build list with choice results.
		final List<ArtificialChoiceResults> achoices = new ArrayList<ArtificialChoiceResults>(size);
		for (final Object[] choice : choices) {
			achoices.add(new ArtificialChoiceResults(choice));
		}
		
		// Create workers.
        workers.clear();
		final ArtificialScoreBoard scoreBoard=new ArtificialScoreBoard();
		final int workerSize = Math.min(size, THREADS);
		for (int index = 0; index < workerSize; index++) {
			final MagicGame workerGame=new MagicGame(sourceGame,scorePlayer);
            if (!CHEAT) {
    			workerGame.setKnownCards();
            }
			workerGame.setFastChoices(true);
			workers.add(new ArtificialWorker(index,workerGame,scoreBoard));
		}
		
		// find optimal number of main phases and best score and first result single-threaded.
		int mainPhases = sourceGame.getArtificialLevel(scorePlayer.getIndex());
		final ArtificialChoiceResults firstChoice = achoices.get(0);
		while (true) {
			pruneScore = createPruneScore();
			processingLeft = 1;
			startWorker(firstChoice,mainPhases,INITIAL_MAX_DEPTH,INITIAL_MAX_GAMES);
			waitUntilProcessed();
			if (mainPhases < 2 || firstChoice.aiScore != ArtificialScore.MAXIMUM_DEPTH_EXCEEDED_SCORE) {
				break;
			}
			scoreBoard.clear();
			mainPhases--;
		} 
		
		// find best score for the other choice results multi-threaded.
		if (size > 1) {
			processingLeft = size-1;
			for (int index = 1; index < size; index++) {
				startWorker(achoices.get(index),mainPhases,MAX_DEPTH,MAX_GAMES);
			}
			waitUntilProcessed();
            workers.clear();
		}
		
		// select the best scoring choice result.
		ArtificialScore bestScore = ArtificialScore.INVALID_SCORE;
		ArtificialChoiceResults bestAchoice = achoices.get(0);
		for (final ArtificialChoiceResults achoice : achoices) {
			if (bestScore.isBetter(achoice.aiScore,true)) {
				bestScore = achoice.aiScore;
				bestAchoice = achoice;				
			}
		}

		// Logging.
		final long time_taken = System.currentTimeMillis() - start_time;
		log("MMAB" + 
                " index=" + scorePlayer.getIndex() +
                " life=" + scorePlayer.getLife() +
                " time=" + time_taken + 
                " workers=" + workerSize + 
                " main=" + mainPhases);
		for (final ArtificialChoiceResults achoice : achoices) {
			log((achoice == bestAchoice ? "* " : "  ") + achoice);
		}

		return sourceGame.map(bestAchoice.choiceResults);
	}
	
	private synchronized void startWorker(
            final ArtificialChoiceResults aiChoiceResults,
            final int mainPhases,
            final int maxDepth,
            final int maxGames) {
		while (workers.isEmpty()) {
			try { //wait
				wait();
			} catch (final InterruptedException ex) {
                throw new RuntimeException(ex);
            }
		}
		final ArtificialWorker worker=workers.removeLast();
		new ArtificialWorkerThread(worker,aiChoiceResults,pruneScore,mainPhases,maxDepth,maxGames).start();
	}
	
	private synchronized void releaseWorker(final ArtificialWorker worker,final ArtificialChoiceResults aiChoiceResults) {
		pruneScore = pruneScore.getPruneScore(aiChoiceResults.aiScore.getScore(),true);
		processingLeft--;
		workers.add(worker);
		notifyAll();
	}
	
	private synchronized void waitUntilProcessed() {
		while (processingLeft > 0) {
			try { //wait
				wait();
			} catch (final InterruptedException ex) {
                throw new RuntimeException(ex);
            }			
		}
	}
	
	private final class ArtificialWorkerThread extends Thread {
        private final ArtificialWorker worker;
		private final ArtificialChoiceResults aiChoiceResults;
		private final ArtificialPruneScore pruneScore;
		private final int mainPhases;
		private final int maxDepth;
		private final int maxGames;
		
		public ArtificialWorkerThread(
                final ArtificialWorker worker,
                final ArtificialChoiceResults aiChoiceResults,
				final ArtificialPruneScore pruneScore,
                final int mainPhases,
                final int maxDepth,
                final int maxGames) {
			
			this.worker=worker;
			this.aiChoiceResults=aiChoiceResults;
			this.pruneScore=pruneScore;
			this.mainPhases=mainPhases;
			this.maxDepth=maxDepth;
			this.maxGames=maxGames;
		}
				
		@Override
		public void run() {
			worker.evaluateGame(aiChoiceResults,pruneScore,mainPhases,maxDepth,maxGames);
			releaseWorker(worker,aiChoiceResults);
		}
	}
}
