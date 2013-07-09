package magic.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import magic.model.MagicGame;
import magic.model.MagicGameLog;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.phase.MagicPhase;

public class MMAB2 implements MagicAI {

    private static final long      SEC_TO_NANO=1000000000L;
    private static final int THREADS = Runtime.getRuntime().availableProcessors();

    private final boolean LOGGING;
    private final boolean CHEAT;
    private ArtificialPruneScore pruneScore = new ArtificialMultiPruneScore();

    MMAB2() {
        //default: no logging, no cheats
        this(false, false);
    }

    MMAB2(final boolean log, final boolean cheat) {
        LOGGING = log || Boolean.getBoolean("debug");
        CHEAT = cheat;
    }

    private void log(final String message) {
        MagicGameLog.log(message);
        if (LOGGING) {
            System.err.println(message);
        }
    }

    public Object[] findNextEventChoiceResults(final MagicGame sourceGame, final MagicPlayer scorePlayer) {
        final long startTime = System.currentTimeMillis();

        // copying the game is necessary because for some choices game scores might be calculated,
        // find all possible choice results.
        MagicGame choiceGame = new MagicGame(sourceGame,scorePlayer);
        final MagicEvent event = choiceGame.getNextEvent();
        final List<Object[]> choices = event.getArtificialChoiceResults(choiceGame);
        final int size = choices.size();
        choiceGame = null;

        assert size != 0 : "ERROR: no choices available for MMAB2";

        // single choice result.
        if (size == 1) {
            return sourceGame.map(choices.get(0));
        }

        // submit jobs
        final ArtificialScoreBoard scoreBoard = new ArtificialScoreBoard();
        final ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        final List<ArtificialChoiceResults> achoices=new ArrayList<ArtificialChoiceResults>();
        final int artificialLevel = sourceGame.getArtificialLevel(scorePlayer.getIndex());
        final int rounds = (size + THREADS - 1) / THREADS;
        final long slice = artificialLevel * SEC_TO_NANO / rounds;
        for (final Object[] choice : choices) {
            final ArtificialChoiceResults achoice=new ArtificialChoiceResults(choice);
            achoices.add(achoice);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    final MagicGame workerGame=new MagicGame(sourceGame,scorePlayer);
                    if (!CHEAT) {
                        workerGame.hideHiddenCards();
                    }
                    workerGame.setFastChoices(true);
                    final MMABWorker worker=new MMABWorker(
                        Thread.currentThread().getId(),
                        workerGame,
                        scoreBoard,
                        CHEAT
                    );
                    worker.evaluateGame(achoice, getPruneScore(), System.nanoTime() + slice);
                    updatePruneScore(achoice.aiScore.getScore());
                }
            });
        }
        executor.shutdown();
        try {
            // wait for artificialLevel * 2 seconds for jobs to finish
            executor.awaitTermination(artificialLevel * 2, TimeUnit.SECONDS);
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
            if (bestScore.isBetter(achoice.aiScore,true)) {
                bestScore = achoice.aiScore;
                bestAchoice = achoice;
            }
        }

        // Logging.
        final long timeTaken = System.currentTimeMillis() - startTime;
        log("MMAB2" +
            " cheat=" + CHEAT +
            " index=" + scorePlayer.getIndex() +
            " life=" + scorePlayer.getLife() +
            " phase=" + sourceGame.getPhase().getType() +
            " slice=" + (slice/1000000) +
            " time=" + timeTaken
            );
        for (final ArtificialChoiceResults achoice : achoices) {
            log((achoice == bestAchoice ? "* " : "  ") + achoice);
        }

        return sourceGame.map(bestAchoice.choiceResults);
    }

    private void updatePruneScore(final int score) {
        pruneScore = pruneScore.getPruneScore(score,true);
    }

    private ArtificialPruneScore getPruneScore() {
        return pruneScore;
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

    private ArtificialScore runGame(final Object[] nextChoiceResults, final ArtificialPruneScore pruneScore, final int depth, final long maxTime) {
        game.startActions();

        if (nextChoiceResults!=null) {
            game.executeNextEvent(nextChoiceResults);
        }

        if (System.nanoTime() > maxTime || Thread.currentThread().isInterrupted()) {
            final ArtificialScore aiScore=new ArtificialScore(game.getScore(),depth);
            game.undoActions();
            gameCount++;
            return aiScore;
        }

        // Play game until given end turn for all possible choices.
        while (!game.isFinished()) {
            if (!game.hasNextEvent()) {
                game.executePhase();

                // Caching of best score for game situations.
                if (game.cacheState()) {
                    final long gameId=game.getGameId(pruneScore.getScore());
                    ArtificialScore bestScore=scoreBoard.getGameScore(gameId);
                    if (bestScore==null) {
                        bestScore=runGame(null,pruneScore,depth,maxTime);
                        scoreBoard.setGameScore(gameId,bestScore.getScore(-depth));
                    } else {
                        bestScore=bestScore.getScore(depth);
                    }
                    game.undoActions();
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
            game.undoActions();
            return bestScore;
        }

        // Game is finished.
        final ArtificialScore aiScore=new ArtificialScore(game.getScore(),depth);
        game.undoActions();
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
