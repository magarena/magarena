package magic.ai;

import java.util.List;

import magic.model.MagicGame;
import magic.model.MagicGameReport;
import magic.model.event.MagicEvent;
import magic.model.phase.MagicPhase;

public class ArtificialWorker {
	
	private static final class MaximumExceededException extends RuntimeException {
		private static final long serialVersionUID = 1L;
		public MaximumExceededException() {
			super("Maximum is exceeded.");
		}
	};
	
	private final int id;
	private final MagicGame game;
	private final ArtificialScoreBoard scoreBoard;
	private int gameCount;
	private int maxDepth;
	private int maxGames;
	
	public ArtificialWorker(final int id,final MagicGame game,final ArtificialScoreBoard scoreBoard) {
		this.id=id;
		this.game=game;
		this.scoreBoard=scoreBoard;
	}
	
	private ArtificialScore runGame(final Object nextChoiceResults[],final ArtificialPruneScore pruneScore,int depth) {
		if (depth>maxDepth) {
			throw new MaximumExceededException();
		}
		
		game.startActions();
		if (nextChoiceResults!=null) {
			game.executeNextEvent(nextChoiceResults);
		}

		// Play game until given end turn for all possible choices.
		while (!game.isFinished()) {
			
			if (game.hasNextEvent()) {
				final MagicEvent event=game.getNextEvent();
				if (event.hasChoice()) {
					final List<Object[]> choiceResultsList=event.getArtificialChoiceResults(game);
					final int nrOfChoices=choiceResultsList.size();
					if (nrOfChoices==0) {
						// No choices, score is invalid.
						game.undoActions();
						return ArtificialScore.INVALID_SCORE;
					} else if (nrOfChoices==1) {
						depth++;
						game.executeNextEvent(choiceResultsList.get(0));
					} else {
						final boolean best=game.getScorePlayer()==event.getPlayer();
						depth+=nrOfChoices;
						ArtificialScore bestScore=ArtificialScore.INVALID_SCORE;
						ArtificialPruneScore newPruneScore=pruneScore;
						for (final Object choiceResults[] : choiceResultsList) {

							final ArtificialScore score=runGame(choiceResults,newPruneScore,depth);
							if (bestScore.isBetter(score,best)) {
								bestScore=score;
								// Stop when best score can no longer become the best score at previous levels.
								if (pruneScore.pruneScore(bestScore.score,best)) {
									break;
								}
								newPruneScore=newPruneScore.getPruneScore(bestScore.score,best);
							}
						}
						game.undoActions();
						return bestScore;
					}
				} else {
					game.executeNextEvent(MagicEvent.NO_CHOICE_RESULTS);
				}
			} else {
				final MagicPhase phase=game.getPhase();
				phase.executePhase(game);
								
				// Caching of best score for game situations.
				if (game.cacheState()) {
					final long gameId=game.getGameId(pruneScore.getScore());
					ArtificialScore bestScore=scoreBoard.getGameScore(gameId);
					if (bestScore==null) {
						bestScore=runGame(null,pruneScore,depth);
						scoreBoard.setGameScore(gameId,bestScore.getScore(-depth));
					} else {
						bestScore=bestScore.getScore(depth);
					}
					game.undoActions();
					return bestScore;
				}				
			}
		}

		// Game is finished.
		final ArtificialScore aiScore=new ArtificialScore(game.getScore(),depth);
		game.undoActions();
		gameCount++;
		if (gameCount>maxGames) {
			throw new MaximumExceededException();
		}
		return aiScore;
	}

	public void evaluateGame(
            final ArtificialChoiceResults aiChoiceResults,
            final ArtificialPruneScore pruneScore,
			final int mainPhases,
            final int maxDepth,
            final int maxGames) {
		gameCount=0;
		this.maxDepth=maxDepth;
		this.maxGames=maxGames;
		game.setMainPhases(mainPhases);
		aiChoiceResults.worker=id;

        try { //check depth exceeded
			aiChoiceResults.aiScore=runGame(game.map(aiChoiceResults.choiceResults),pruneScore,0);
		} catch (final MaximumExceededException ex) {
			aiChoiceResults.aiScore=ArtificialScore.MAXIMUM_DEPTH_EXCEEDED_SCORE;
		} 

		aiChoiceResults.gameCount=gameCount;
		game.undoAllActions();
	}	
}
