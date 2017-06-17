package magic.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import magic.model.MagicGame;
import magic.model.MagicGameLog;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.phase.MagicStep;

public class MMAB extends MagicAI {

    private static final long SEC_TO_NANO=1000000000L;

    private final boolean CHEAT;
    private final boolean DECKSTR;

    public MMAB(final boolean cheat) {
        this(cheat, false);
    }

    public static MMAB DeckStrAI() {
        return new MMAB(false, true);
    }

    private MMAB(final boolean cheat, final boolean deckStr) {
        CHEAT = cheat;
        DECKSTR = deckStr;
    }

    private void log(final String message) {
        MagicGameLog.log(message);
    }

    @Override
    public Object[] findNextEventChoiceResults(final MagicGame sourceGame, final MagicPlayer scorePlayer) {
        final long startTime = System.currentTimeMillis();

        // copying the game is necessary because for some choices game scores might be calculated,
        // find all possible choice results.
        MagicGame choiceGame = new MagicGame(sourceGame,scorePlayer);
        final MagicEvent event = choiceGame.getNextEvent();
        final List<Object[]> choices = event.getArtificialChoiceResults(choiceGame);
        final int size = choices.size();

        assert size != 0 : "ERROR: no choices available for MMAB";

        // single choice result.
        if (size == 1) {
            return sourceGame.map(choices.get(0));
        }

        // submit jobs
        final ArtificialPruneScoreRef scoreRef = new ArtificialPruneScoreRef(new ArtificialMultiPruneScore());
        final ArtificialScoreBoard scoreBoard = new ArtificialScoreBoard();
        final ExecutorService executor = Executors.newFixedThreadPool(getMaxThreads());
        final List<ArtificialChoiceResults> achoices=new ArrayList<ArtificialChoiceResults>();
        final int artificialLevel = scorePlayer.getAiProfile().getAiLevel();
        final int rounds = (size + getMaxThreads() - 1) / getMaxThreads();
        final long slice = artificialLevel * SEC_TO_NANO / rounds;

        for (final Object[] choice : choices) {
            final ArtificialChoiceResults achoice=new ArtificialChoiceResults(choice);
            achoices.add(achoice);

            final MagicGame workerGame=new MagicGame(sourceGame,scorePlayer);
            if (!CHEAT) {
                workerGame.hideHiddenCards();
            }
            if (DECKSTR) {
                workerGame.setMainPhases(artificialLevel);
            }
            workerGame.setFastMana(true);
            workerGame.setFastTarget(true);
            workerGame.setFastBlocker(true);

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    final MMABWorker worker=new MMABWorker(
                        Thread.currentThread().getId(),
                        workerGame,
                        scoreBoard,
                        CHEAT
                    );
                    worker.evaluateGame(achoice, scoreRef.get(), System.nanoTime() + slice);
                    scoreRef.update(achoice.aiScore.getScore());
                }
            });
        }

        executor.shutdown();
        try {
            // wait for artificialLevel + 1 seconds for jobs to finish
            executor.awaitTermination(artificialLevel + 1, TimeUnit.SECONDS);
        } catch (final InterruptedException ex) {
            throw new RuntimeException(ex);
        } finally {
            // force termination of workers
            executor.shutdownNow();
        }

        // select the best scoring choice result.
        ArtificialScore bestScore = ArtificialScore.INVALID_SCORE;
        ArtificialChoiceResults bestAchoice = achoices.get(0);
        for (final ArtificialChoiceResults achoice : achoices) {
            if (bestScore.isBetter(achoice.aiScore, true) &&
                MovesBlackList.isBlackListed(choiceGame, event, achoice.choiceResults) == false) {
                bestScore = achoice.aiScore;
                bestAchoice = achoice;
            }
        }

        // Logging.
        final long timeTaken = System.currentTimeMillis() - startTime;
        log("MMAB" +
            " cheat=" + CHEAT +
            " index=" + scorePlayer.getIndex() +
            " life=" + scorePlayer.getLife() +
            " turn=" + sourceGame.getTurn() +
            " phase=" + sourceGame.getPhase().getType() +
            " slice=" + (slice/1000000) +
            " time=" + timeTaken
            );
        for (final ArtificialChoiceResults achoice : achoices) {
            log((achoice == bestAchoice ? "* " : "  ") + achoice);
        }

        return sourceGame.map(bestAchoice.choiceResults);
    }

class MMABWorker {

    private final boolean CHEAT;
    private final long id;
    private final MagicGame game;
    private final ArtificialScoreBoard scoreBoard;

    private int gameCount;

    MMABWorker(final long id,final MagicGame game,final ArtificialScoreBoard scoreBoard, final boolean CHEAT) {
        this.id=id;
        this.game=game;
        this.scoreBoard=scoreBoard;
        this.CHEAT=CHEAT;
    }

    /** Determines if game score should be cached for this game state. */
    public boolean shouldCache() {
        switch (game.getPhase().getType()) {
            case FirstMain:
            case EndOfCombat:
            case Cleanup:
                return game.getStep()==MagicStep.NextPhase;
            default:
                return false;
        }
    }

    private ArtificialScore runGame(final Object[] nextChoiceResults, final ArtificialPruneScore pruneScore, final int depth, final long maxTime) {
        game.snapshot();

        if (nextChoiceResults!=null) {
            game.executeNextEvent(nextChoiceResults);
        }

        if (System.nanoTime() > maxTime || Thread.currentThread().isInterrupted()) {
            final ArtificialScore aiScore=new ArtificialScore(game.getScore(),depth);
            game.restore();
            gameCount++;
            return aiScore;
        }

        // Play game until given end turn for all possible choices.
        while (!game.isFinished()) {
            if (!game.hasNextEvent()) {
                game.executePhase();

                // Caching of best score for game situations.
                if (shouldCache()) {
                    final long gameId=game.getGameId(pruneScore.getScore());
                    ArtificialScore bestScore=scoreBoard.getGameScore(gameId);
                    if (bestScore==null) {
                        bestScore=runGame(null,pruneScore,depth,maxTime);
                        scoreBoard.setGameScore(gameId,bestScore.getScore(-depth));
                    } else {
                        bestScore=bestScore.getScore(depth);
                    }
                    game.restore();
                    return bestScore;
                }
                continue;
            }

            final MagicEvent event=game.getNextEvent();

            if (!event.hasChoice()) {
                game.executeNextEvent();
                continue;
            }

            //final long startExpansion = System.nanoTime();
            final List<Object[]> choiceResultsList=event.getArtificialChoiceResults(game);
            //final long timeExpansion = System.nanoTime() - startExpansion;

            /*
            System.out.println(
                "EXPANSION" +
                " cheat=" + CHEAT +
                " choice=" + event.getChoice().getClass().getSimpleName() +
                " time=" + timeExpansion
            );
            */

            final int nrOfChoices=choiceResultsList.size();

            assert nrOfChoices > 0 : "nrOfChoices is 0";

            if (nrOfChoices==1) {
                game.executeNextEvent(choiceResultsList.get(0));
                continue;
            }

            final boolean best=game.getScorePlayer()==event.getPlayer();
            ArtificialScore bestScore=ArtificialScore.INVALID_SCORE;
            ArtificialPruneScore newPruneScore=pruneScore;
            long end = System.nanoTime();
            final long slice = (maxTime - end) / nrOfChoices;
            for (final Object[] choiceResults : choiceResultsList) {
                end += slice;
                final ArtificialScore score=runGame(choiceResults, newPruneScore, depth + 1, end);
                if (bestScore.isBetter(score,best)) {
                    bestScore=score;
                    // Stop when best score can no longer become the best score at previous levels.
                    if (pruneScore.pruneScore(bestScore.getScore(),best)) {
                        break;
                    }
                    newPruneScore=newPruneScore.getPruneScore(bestScore.getScore(),best);
                }
            }
            game.restore();
            return bestScore;
        }

        // Game is finished.
        final ArtificialScore aiScore=new ArtificialScore(game.getScore(),depth);
        game.restore();
        gameCount++;
        return aiScore;
    }

    void evaluateGame(final ArtificialChoiceResults aiChoiceResults, final ArtificialPruneScore pruneScore, long maxTime) {
        gameCount = 0;

        aiChoiceResults.worker    = id;
        aiChoiceResults.aiScore   = runGame(game.map(aiChoiceResults.choiceResults),pruneScore,0,maxTime);
        aiChoiceResults.gameCount = gameCount;

        game.undoAllActions();
    }
}
}
