package magic.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;

public class ArtificialWorkerPool {
	
	public static final int MAX_LEVEL=8;
	
	private static final int THREADS=getNrOfThreads();
	private static final int INITIAL_MAX_DEPTH=110;
	private static final int INITIAL_MAX_GAMES=10000;
	private static final int MAX_DEPTH=120;
	private static final int MAX_GAMES=12000;
	private static final boolean LOGGING=false;
	
	private final MagicGame sourceGame;
	private final MagicPlayer scorePlayer;
	private final LinkedList<ArtificialWorker> workers;
	private int processingLeft;
	private ArtificialPruneScore pruneScore;

	public ArtificialWorkerPool(final MagicGame game,final MagicPlayer scorePlayer) {

		sourceGame=game;
		this.scorePlayer=scorePlayer;
		workers=new LinkedList<ArtificialWorker>();
	}
	
	private static void logMessage(final String message) {
		
		if (LOGGING) {
			System.out.println(message);
		}
	}
	
	private static final int getNrOfThreads() {
		
		// Use maximum 4 artificial worker threads.
		final int threads=Math.min(4,Runtime.getRuntime().availableProcessors()); 
		logMessage(threads+" AI worker threads.");
		return threads;
	}
	
	private ArtificialPruneScore createPruneScore() {
		
		return new ArtificialMultiPruneScore();
	}
	
	public synchronized Object[] findNextEventChoiceResults() {

		// Logging
		long time=System.currentTimeMillis();

		// Copying the game is necessary because for some choices game scores might be calculated.
		MagicGame choiceGame=new MagicGame(sourceGame,scorePlayer);
		choiceGame.setKnownCards();
		final MagicEvent event=choiceGame.getNextEvent();

		// Find all possible choice results.
		final List<Object[]> choiceResultsList=event.getArtificialChoiceResults(choiceGame);
		choiceGame=null;
		// No choice results.
		if (choiceResultsList.size()==0) {
			return null;
		}
		
		final int size=choiceResultsList.size();
		// Single choice result.
		if (size==1) {
			final Object bestChoiceResults[]=choiceResultsList.get(0);
			logMessage("Single : "+Arrays.toString(bestChoiceResults));
			return sourceGame.map(bestChoiceResults);
		}
		
		// Build list with choice results.
		final List<ArtificialChoiceResults> aiChoiceResultsList=new ArrayList<ArtificialChoiceResults>();
		for (final Object choiceResults[] : choiceResultsList) {

			aiChoiceResultsList.add(new ArtificialChoiceResults(choiceResults));
		}
		
		// Create workers.
		final ArtificialScoreBoard scoreBoard=new ArtificialScoreBoard();
		final int workerSize=size>THREADS?THREADS:size;
		for (int index=0;index<workerSize;index++) {

			final MagicGame workerGame=new MagicGame(sourceGame,scorePlayer);
			workerGame.setKnownCards();
			workerGame.setFastChoices(true);
			workers.add(new ArtificialWorker(index,workerGame,scoreBoard));
		}
		
		// Find optimal number of main phases and best score and first result single-threaded.
		int mainPhases=sourceGame.getArtificialLevel();
		final ArtificialChoiceResults firstAiChoiceResults=aiChoiceResultsList.get(0);
		while (true) {
			pruneScore=createPruneScore();
			processingLeft=1;
			startWorker(firstAiChoiceResults,mainPhases,INITIAL_MAX_DEPTH,INITIAL_MAX_GAMES);
			waitUntilProcessed();
			if (mainPhases<2||firstAiChoiceResults.aiScore!=ArtificialScore.MAXIMUM_DEPTH_EXCEEDED_SCORE) {
				break;
			}
			scoreBoard.clear();
			mainPhases--;
		} 
		
		// Find best score for the other choice results multi-threaded.
		if (size>1) {
			processingLeft=size-1;
			for (int index=1;index<size;index++) {
				
				startWorker(aiChoiceResultsList.get(index),mainPhases,MAX_DEPTH,MAX_GAMES);
			}
			waitUntilProcessed();
		}
		
		// Select the best scoring choice result.
		ArtificialScore bestScore=ArtificialScore.INVALID_SCORE;
		ArtificialChoiceResults bestChoiceResults=aiChoiceResultsList.get(0);
		for (final ArtificialChoiceResults aiChoiceResults : aiChoiceResultsList) {
		
			if (bestScore.isBetter(aiChoiceResults.aiScore,true)) {
				bestScore=aiChoiceResults.aiScore;
				bestChoiceResults=aiChoiceResults;				
			}
		}

		// Logging.
		time=System.currentTimeMillis()-time;
		logMessage("Time : "+time+"  Workers : "+workerSize+"  Main : "+mainPhases);
		for (final ArtificialChoiceResults aiChoiceResults : aiChoiceResultsList) {
			
			logMessage((aiChoiceResults==bestChoiceResults?"* ":"  ")+aiChoiceResults);
		}

		return sourceGame.map(bestChoiceResults.choiceResults);
	}
	
	private synchronized void startWorker(final ArtificialChoiceResults aiChoiceResults,final int mainPhases,final int maxDepth,final int maxGames) {
		
		while (workers.isEmpty()) {
			
			try {
				wait();
			} catch (final Exception ex) {}
		}
		final ArtificialWorker worker=workers.removeLast();
		new ArtificialWorkerThread(worker,aiChoiceResults,pruneScore,mainPhases,maxDepth,maxGames).start();
	}
	
	private synchronized void releaseWorker(final ArtificialWorker worker,final ArtificialChoiceResults aiChoiceResults) {

		pruneScore=pruneScore.getPruneScore(aiChoiceResults.aiScore.score,true);
		processingLeft--;
		workers.add(worker);
		notifyAll();
	}
	
	private synchronized void waitUntilProcessed() {
		
		while (processingLeft>0) {
			
			try {
				wait();
			} catch (final Exception ex) {
				ex.printStackTrace();
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
		
		public ArtificialWorkerThread(final ArtificialWorker worker,final ArtificialChoiceResults aiChoiceResults,
				final ArtificialPruneScore pruneScore,final int mainPhases,final int maxDepth,final int maxGames) {
			
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