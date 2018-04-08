package magic.ai;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import magic.model.MagicGame;
import magic.model.MagicGameLog;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;

/*
 * MTD(f) algorithm by Aske Plaat
 * https://askeplaat.wordpress.com/534-2/mtdf-algorithm/
 */
public class MTDF extends MagicAI {

    private final boolean CHEAT;
    private final Map<Long,TTEntry> table = new HashMap<>();

    private long END;

    public MTDF(final boolean aCheat) {
        CHEAT = aCheat;
    }

    @Override
    public Object[] findNextEventChoiceResults(final MagicGame sourceGame, final MagicPlayer scorePlayer) {
        final int artificialLevel = scorePlayer.getAiProfile().getAiLevel();
        final long startTime = System.currentTimeMillis();

        END = startTime + artificialLevel * 1000;

        final MagicGame root = new MagicGame(sourceGame, scorePlayer);
        //root.setMainPhases(artificialLevel);

        if (!CHEAT) {
            root.hideHiddenCards();
        }

        final MagicEvent event = root.getNextEvent();
        final List<Object[]> choices = event.getArtificialChoiceResults(root);
        if (choices.size() == 1) {
            return sourceGame.map(choices.get(0));
        }

        root.setFastChoices(true);
        final TTEntry result = iterative_deepening(root, choices);

        // Logging.
        final long timeTaken = System.currentTimeMillis() - startTime;
        log("MTDF" +
            " cheat=" + CHEAT +
            " index=" + scorePlayer.getIndex() +
            " life=" + scorePlayer.getLife() +
            " turn=" + sourceGame.getTurn() +
            " phase=" + sourceGame.getPhase().getType() +
            " time=" + timeTaken
            );

        final Object[] chosen = choices.get(result.chosen);
        for (final Object[] choice : choices) {
            final StringBuilder buf = new StringBuilder();
            ArtificialChoiceResults.appendResult(choice, buf);
            log((choice == chosen ? "* " : "  ") + buf);
        }

        return sourceGame.map(chosen);
    }

    private boolean hasTime() {
        return System.currentTimeMillis() < END;
    }

    private TTEntry iterative_deepening(final MagicGame root, final List<Object[]> choices) {
        TTEntry result = null;
        int firstguess = 0;
        for (int d = 1; hasTime(); d++) {
            firstguess = MTDF(root, choices, firstguess, d);
            if (hasTime()) {
                result = table.get(root.getStateId());
            }
        }
        return result;
    }

    private int MTDF(final MagicGame root, final List<Object[]> choices, int f, int d) {
        int g = f;
        int lowerbound = Integer.MIN_VALUE;
        int upperbound = Integer.MAX_VALUE;
        table.clear();
        while (lowerbound < upperbound) {
            int beta = (g == lowerbound) ? g + 1 : g;
            g = AlphaBetaWithMemory(root, choices, beta - 1, beta, d);
            if (g < beta) {
                upperbound = g;
            } else {
                lowerbound = g;
            }
        }
        return g;
    }

    private int AlphaBetaWithMemory(final MagicGame game, final List<Object[]> choices, int alpha, int beta, int d) {
        /* Transposition table lookup */
        final long id = game.getStateId();
        TTEntry entry = table.get(id);
        if (entry != null) {
            if (entry.lowerbound >= beta) {
                return entry.lowerbound;
            }
            if (entry.upperbound <= alpha) {
                return entry.upperbound;
            }
            alpha = Math.max(alpha, entry.lowerbound);
            beta = Math.min(beta, entry.upperbound);
        } else {
            entry = new TTEntry();
            table.put(id, entry);
        }

        if (d == 0 || game.isFinished() || !hasTime()) {
            /* leaf node */
            int g = game.getScore();
            entry.update(g, alpha, beta);
            return g;
        }

        final boolean isMax = game.getScorePlayer() == game.getNextEvent().getPlayer();
        final boolean isMin = !isMax;

        int g = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int a = alpha; /* save original alpha value */
        int b = beta;  /* save original beta value */
        int idx = -1;

        for (final Object[] choice : choices) {
            if ((isMax && g >= beta) ||
                (isMin && g <= alpha)) {
                break;
            }

            game.snapshot();
            game.executeNextEvent(choice);
            final List<Object[]> choices_child = d == 1 ?
                Collections.<Object[]>emptyList():
                game.advanceToNextEventWithChoices();
            final int g_child = AlphaBetaWithMemory(game, choices_child, a, b, d - 1);
            game.restore();

            idx++;
            if ((isMax && g_child > g) ||
                (isMin && g_child < g)) {
                g = g_child;
                entry.chosen = idx;
            }

            if (isMax) {
                a = Math.max(a, g);
            } else {
                b = Math.min(b, g);
            }
        }

        final long id_check = game.getStateId();
        if (id != id_check) {
            table.put(id_check, entry);
            table.remove(id);
        }

        entry.update(g, alpha, beta);
        return g;
    }

    private void log(final String message) {
        MagicGameLog.log(message);
    }
}

class TTEntry {
    int lowerbound = Integer.MIN_VALUE;
    int upperbound = Integer.MAX_VALUE;
    int chosen = -1;

    void update(int g, int alpha, int beta) {
        /* Traditional transposition table storing of bounds */
        /* Fail low result implies an upper bound */
        if (g <= alpha) {
            upperbound = g;
        }
        /* Found an accurate minimax value - will not occur if called with zero window */
        if (g > alpha && g < beta) {
            lowerbound = g;
            upperbound = g;
        }
        /* Fail high result implies a lower bound */
        if (g >= beta) {
            lowerbound = g;
        }
    }
}
