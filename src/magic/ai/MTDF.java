package magic.ai;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

/*
 * MTD(f) algorithm by Aske Plaat
 * https://askeplaat.wordpress.com/publications/mtdf-algorithm/
 */
public class MTDF implements MagicAI {

    private static final int MAX_SEARCH_DEPTH = 100;

    public Object[] findNextEventChoiceResults(final MagicGame sourceGame, final MagicPlayer scorePlayer) {
        return new Object[0];
    }

    private int iterative_deepening(final Node root) {
        int firstguess = 0;
        for (int d = 1; d <= MAX_SEARCH_DEPTH; d++) {
            firstguess = MTDF(root, firstguess, d);
            if (times_up()) {
                break;
            }
        }
        return firstguess;
    }

    private int MTDF(final Node root, int f, int d) {
        int g = f;
        int upperbound = Integer.MAX_VALUE;
        int lowerbound = Integer.MIN_VALUE;
        while (lowerbound < upperbound) {
            int beta = (g == lowerbound) ? g + 1 : g;
            g = AlphaBetaWithMemory(root, beta - 1, beta, d);
            if (g < beta) {
                upperbound = g;
            } else {
                lowerbound = g;
            }
        }
        return g;
    }

    private int AlphaBetaWithMemory(final Node n, int alpha , int beta , int d) {
        /* Transposition table lookup */
        if (retrieve(n)) {
            if (n.lowerbound >= beta) {
                return n.lowerbound;
            }
            if (n.upperbound <= alpha) {
                return n.upperbound;
            }
            alpha = Math.max(alpha, n.lowerbound);
            beta = Math.min(beta, n.upperbound);
        }
        int g = 0;
        if (d == 0) {
            /* leaf node */
            g = evaluate(n);
        } else if (n.isMaxNode) {
            g = Integer.MIN_VALUE;
            int a = alpha; /* save original alpha value */
            Node c = firstchild(n);
            while (g < beta && c != null) {
                g = Math.max(g, AlphaBetaWithMemory(c, a, beta, d - 1));
                a = Math.max(a, g);
                c = nextbrother(c);
            }
        } else {
            /* n is a MINNODE */
            g = Integer.MAX_VALUE;
            int b = beta; /* save original beta value */
            Node c = firstchild(n);
            while (g > alpha && c != null) {
                g = Math.min(g, AlphaBetaWithMemory(c, alpha, b, d - 1));
                b = Math.min(b, g);
                c = nextbrother(c);
            }
        }
        /* Traditional transposition table storing of bounds */ 
        /* Fail low result implies an upper bound */ 
        if (g <= alpha) {
            n.upperbound = g; 
            store(n.upperbound);
        }
        /* Found an accurate minimax value - will not occur if called with zero window */ 
        if (g > alpha && g < beta) {
            n.lowerbound = g; 
            n.upperbound = g; 
            store(n.lowerbound);
            store(n.upperbound);
        }
        /* Fail high result implies a lower bound */ 
        if (g >= beta) {
            n.lowerbound = g; 
            store(n.lowerbound);
        }
        return g;
    }

    private Node nextbrother(final Node n) {
        return null;
    }

    private void store(int n) {

    }
    
    private boolean retrieve(Node n) {
        return false;
    }

    private boolean times_up() {
        return false;
    }

    private int evaluate(Node n) {
        return 0;
    }

    private Node firstchild(Node n) {
        return null;
    }
}

class Node {
    int upperbound;
    int lowerbound;
    boolean isMaxNode;
}
