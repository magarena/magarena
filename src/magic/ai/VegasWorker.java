package magic.ai;

import magic.model.MagicGame;
import magic.model.event.MagicEvent;

public class VegasWorker implements Runnable {

    private static final int MAIN_PHASES=6;

    private final MagicGame sourceGame;
    private final VegasScore score;
    private final Object[] choiceResults;
    private final long slice;
    private final boolean CHEAT;

    VegasWorker(final boolean cheat, final MagicGame sourceGame, final VegasScore score,final long slice) {
        this.CHEAT = cheat;
        this.sourceGame=sourceGame;
        this.score=score;
        this.choiceResults=score.getChoiceResults();
        this.slice=slice;
    }

    /** Play game until number of main phases are completed or until the game is finished. */
    private void runGame(final MagicGame game) {
        while (game.advanceToNextEventWithChoice()) {
            final MagicEvent event = game.getNextEvent();
            final Object[] result = event.getSimulationChoiceResult(game);
            game.executeNextEvent(result);
        }
    }

    @Override
    public void run() {
        final long endTime = System.nanoTime() + slice;
        while (System.nanoTime() < endTime) {
            final MagicGame game = new MagicGame(sourceGame, sourceGame.getScorePlayer());
            if (!CHEAT) {
                game.showRandomizedHiddenCards();
            }
            game.setMainPhases(MAIN_PHASES);
            game.executeNextEvent(game.map(choiceResults));
            runGame(game);
            score.incrementScore(game.getScore());
        }
    }
}
