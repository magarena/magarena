package magic.ai;

import magic.model.MagicGame;
import magic.model.event.MagicEvent;
import magic.model.MagicPlayer;
import magic.exception.GameException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

/*
 * MTD(f) algorithm by Aske Plaat
 * https://askeplaat.wordpress.com/534-2/mtdf-algorithm/
 */
public class MTDF implements MagicAI {

    private static final int MAX_SEARCH_DEPTH = 100;
    private final Map<Long,TTEntry> table = new HashMap<>();

    public Object[] findNextEventChoiceResults(final MagicGame sourceGame, final MagicPlayer scorePlayer) {
        final int artificialLevel = sourceGame.getArtificialLevel(scorePlayer.getIndex());
        final long end = System.currentTimeMillis() + artificialLevel * 1000;

        final MagicGame root = new MagicGame(sourceGame, scorePlayer);
        
        final MagicEvent event = root.getNextEvent();
        final List<Object[]> choices = event.getArtificialChoiceResults(root);
        if (choices.size() == 1) {
            return sourceGame.map(choices.get(0));
        }

        final TTEntry entry = iterative_deepening(root, end);

        System.err.println("MTDF chosen: " + entry.chosen + " " + entry.lowerbound + " " + entry.lowerbound);

        return sourceGame.map(choices.get(entry.chosen));
    }

    private TTEntry iterative_deepening(final MagicGame root, final long end) {
        final long stateId = root.getGameId(0);
        int firstguess = 1;
        for (int d = 1; d <= MAX_SEARCH_DEPTH; d++) {
            table.clear();
            firstguess = MTDF(root, firstguess, d);
            if (System.currentTimeMillis() > end) {
                break;
            }
            System.err.println("MTDF guess: " + firstguess + " depth: " + d);
        }
        return table.get(stateId);
    }

    private int MTDF(final MagicGame root, int f, int d) {
        int g = f;
        int lowerbound = Integer.MIN_VALUE;
        int upperbound = Integer.MAX_VALUE;
        while (lowerbound < upperbound) {
            int beta = (g == lowerbound) ? g + 1 : g;
            g = AlphaBetaWithMemory(root, beta - 1, beta, d);
            System.err.println("state = " + root.getGameId(0));
            if (g < beta) {
                upperbound = g;
            } else {
                lowerbound = g;
            }
        }
        return g;
    }

    private int AlphaBetaWithMemory(final MagicGame game, int alpha, int beta, int d) {
        /* Transposition table lookup */
        final long id = game.getGameId(0);
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

        if (d == 0 || game.isFinished()) {
            /* leaf node */
            int g = game.getScore();
            entry.update(g, alpha, beta);
            return g;
        } 
        
        final MagicEvent event = game.getNextEvent();
        final List<Object[]> results = event.getArtificialChoiceResults(game);
        final boolean isMax = game.getScorePlayer() == event.getPlayer();
        final boolean isMin = !isMax;
            
        int g = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int a = alpha; /* save original alpha value */
        int b = beta;  /* save original beta value */
        int idx = -1;
        
        for (final Object[] result : results) {
            if ((isMax && g >= beta) ||
                (isMin && g <= alpha)) {
                break;
            }
            idx++;
            game.snapshot();
            game.executeNextEvent(result);
            game.advanceToNextEventWithChoice();
            final int g_child = AlphaBetaWithMemory(game, a, b, d - 1);
            if ((isMax && g_child > g) ||
                (isMin && g_child < g)) {
                g = g_child;
                entry.chosen = idx;
            }
            game.restore();
            final long id_check = game.getGameId(0);
            if (id != id_check) {
                throw new GameException(new RuntimeException(id + " != " + id_check), game);
            }

            if (isMax) {
                a = Math.max(a, g);
            } else {
                b = Math.min(b, g);
            }
        }
        
        entry.update(g, alpha, beta);
        return g;
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
