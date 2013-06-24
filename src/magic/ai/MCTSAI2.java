package magic.ai;

import magic.data.LRUCache;
import magic.model.MagicGame;
import magic.model.MagicGameLog;
import magic.model.MagicPlayer;
import magic.model.choice.MagicBuilderPayManaCostResult;
import magic.model.event.MagicEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/*
AI using Monte Carlo Tree Search

Classical MCTS (UCT)
 - use UCB1 formula for selection with C = sqrt(2)
 - reward either 0 or 1
 - backup by averaging
 - uniform random simulated playout
 - score = XX% (25000 matches against MMAB-1)

Enchancements to basic UCT
 - use ratio selection (v + 10)/(n + 10)
 - UCB1 with C = 1.0
 - UCB1 with C = 2.0
 - UCB1 with C = 3.0
 - use normal bound max(1,v + 2 * std(v))
 - reward depends on length of playout
 - backup by robust max

References:
UCT algorithm from Kocsis and Sezepesvari 2006

Consistency Modifications for Automatically Tuned Monte-Carlo Tree Search
  consistent -> child of root with greatest number of simulations is optimal
  frugal -> do not need to visit the whole tree
  eps-greedy is not consisteny for fixed eps (with prob eps select randomly, else use score)
  eps-greedy is consistent but not frugal if eps dynamically decreases to 0
  UCB1 is consistent but not frugal
  score = average is not consistent
  score = (total reward + K)/(total simulation + 2K) is consistent and frugal!
  using v_t threshold ensures consistency for case of reward in {0,1} using any score function
    v(s) < v_t (0.3), randomy pick a child, else pick child that maximize score

Monte-Carlo Tree Search in Lines of Action
  1-ply lookahread to detect direct win for player to move
  secure child formula for decision v + A/sqrt(n)
  evaluation cut-off: use score function to stop simulation early
  use evaluation score to remove "bad" moves during simulation
  use evaluation score to keep k-best moves
  mixed: start with corrective, rest of the moves use greedy
*/
public class MCTSAI2 implements MagicAI {

    private static int MIN_SCORE = Integer.MAX_VALUE;
    static int MIN_SIM = Integer.MAX_VALUE;
    private static final int MAX_ACTIONS = 10000;
    static double UCB1_C = 0.4;
    static double RATIO_K = 1.0;

    static {
        if (System.getProperty("min_sim") != null) {
            MIN_SIM = Integer.parseInt(System.getProperty("min_sim"));
            System.err.println("MIN_SIM = " + MIN_SIM);
        }

        if (System.getProperty("min_score") != null) {
            MIN_SCORE = Integer.parseInt(System.getProperty("min_score"));
            System.err.println("MIN_SCORE = " + MIN_SCORE);
        }

        if (System.getProperty("ucb1_c") != null) {
            UCB1_C = Double.parseDouble(System.getProperty("ucb1_c"));
            System.err.println("UCB1_C = " + UCB1_C);
        }

        if (System.getProperty("ratio_k") != null) {
            RATIO_K = Double.parseDouble(System.getProperty("ratio_k"));
            System.err.println("RATIO_K = " + RATIO_K);
        }
    }

    private final boolean LOGGING = Boolean.getBoolean("debug");
    private final boolean CHEAT;

    //cache the set of choices at the root to avoid recomputing it all the time
    private List<Object[]> RCHOICES;

    //cache nodes to reuse them in later decision
    private final LRUCache<Long, MCTSGameTree> CACHE = new LRUCache<Long, MCTSGameTree>(1000);

    public MCTSAI2(final boolean cheat) {
        CHEAT = cheat;
    }

    private void log(final String message) {
        MagicGameLog.log(message);
        if (LOGGING) {
            System.err.println(message);
        }
    }

    public Object[] findNextEventChoiceResults(
            final MagicGame startGame,
            final MagicPlayer scorePlayer) {

        // Determine possible choices
        MagicGame choiceGame = new MagicGame(startGame, scorePlayer);
        final MagicEvent event = choiceGame.getNextEvent();
        RCHOICES = event.getArtificialChoiceResults(choiceGame);
        choiceGame = null;

        final int size = RCHOICES.size();

        // No choice
        assert size > 0 : "ERROR! No choice found at start of MCTS";

        // Single choice
        if (size == 1) {
            return startGame.map(RCHOICES.get(0));
        }

        //normal: max time is 1000 * level
        int MAX_TIME = 1000 * startGame.getArtificialLevel(scorePlayer.getIndex());
        int MAX_SIM = Integer.MAX_VALUE;

        final long START_TIME = System.currentTimeMillis();

        //root represents the start state
        final MCTSGameTree root = MCTSGameTree.getNode(CACHE, startGame, RCHOICES);

        log("MCTS cached=" + root.getNumSim());

        //end simulations once root is AI win or time is up
        int sims;
        for (sims = 0;
             System.currentTimeMillis() - START_TIME < MAX_TIME &&
             sims < MAX_SIM &&
             !root.isAIWin();
             sims++) {

            //clone the MagicGame object for simulation
            final MagicGame rootGame = new MagicGame(startGame, scorePlayer);
            if (!CHEAT) {
                rootGame.hideHiddenCards();
            }

            //pass in a clone of the state,
            //genNewTreeNode grows the tree by one node
            //and returns the path from the root to the new node
            final LinkedList<MCTSGameTree> path = growTree(root, rootGame);

            assert path.size() >= 2 : "ERROR! length of MCTS path is " + path.size();

            // play a simulated game to get score
            // update all nodes along the path from root to new node
            final double score = randomPlay(path.getLast(), rootGame);

            // update score and game theoretic value along the chosen path
            MCTSGameTree child = null;
            MCTSGameTree parent = null;
            while (!path.isEmpty()) {
                child = parent;
                parent = path.removeLast();

                parent.updateScore(child, score);

                if (child != null && child.isSolved()) {
                    final int steps = child.getSteps() + 1;
                    if (parent.isAI() && child.isAIWin()) {
                        parent.setAIWin(steps);
                    } else if (parent.isOpp() && child.isAILose()) {
                        parent.setAILose(steps);
                    } else if (parent.isAI() && child.isAILose()) {
                        parent.incLose(steps);
                    } else if (parent.isOpp() && child.isAIWin()) {
                        parent.incLose(steps);
                    }
                }
            }
        }

        assert root.size() > 0 : "ERROR! Root has no children but there are " + size + " choices";

        //select the best child/choice
        final MCTSGameTree first = root.first();
        double maxD = first.getDecision();
        int bestC = first.getChoice();
        for (final MCTSGameTree node : root) {
            final double D = node.getDecision();
            final int C = node.getChoice();
            if (D > maxD) {
                maxD = D;
                bestC = C;
            }
        }

        log(outputChoice(scorePlayer, root, START_TIME, bestC, sims));

        return startGame.map(RCHOICES.get(bestC));
    }

    private String outputChoice(
            final MagicPlayer scorePlayer,
            final MCTSGameTree root,
            final long START_TIME,
            final int bestC,
            final int sims) {

        final StringBuilder out = new StringBuilder();
        final long duration = System.currentTimeMillis() - START_TIME;

        out.append("MCTS2" +
                   " cheat=" + CHEAT +
                   " index=" + scorePlayer.getIndex() +
                   " life=" + scorePlayer.getLife() +
                   " time=" + duration +
                   " sims=" + sims);
        out.append('\n');

        for (final MCTSGameTree node : root) {
            if (node.getChoice() == bestC) {
                out.append("* ");
            } else {
                out.append("  ");
            }
            out.append('[');
            out.append((int)(node.getV() * 100));
            out.append('/');
            out.append(node.getNumSim());
            out.append('/');
            if (node.isAIWin()) {
                out.append("win");
                out.append(':');
                out.append(node.getSteps());
            } else if (node.isAILose()) {
                out.append("lose");
                out.append(':');
                out.append(node.getSteps());
            } else {
                out.append("?");
            }
            out.append(']');
            out.append(CR2String(RCHOICES.get(node.getChoice())));
            out.append('\n');
        }
        return out.toString().trim();
    }

    private LinkedList<MCTSGameTree> growTree(final MCTSGameTree root, final MagicGame game) {
        final LinkedList<MCTSGameTree> path = new LinkedList<MCTSGameTree>();
        boolean found = false;
        MCTSGameTree curr = root;
        path.add(curr);

        for (List<Object[]> choices = getNextChoices(game, false);
             !choices.isEmpty();
             choices = getNextChoices(game, false)) {

            assert choices.size() > 0 : "ERROR! No choice at start of genNewTreeNode";

            assert !curr.hasDetails() || MCTSGameTree.checkNode(curr, choices) :
                "ERROR! Inconsistent node found" + "\n" +
                game + " " +
                printPath(path) + " " +
                MCTSGameTree.printNode(curr, choices);

            final MagicEvent event = game.getNextEvent();

            //first time considering the choices available at this node,
            //fill in additional details for curr
            if (!curr.hasDetails()) {
                curr.setIsAI(game.getScorePlayer() == event.getPlayer());
                curr.setMaxChildren(choices.size());
                assert curr.setChoicesStr(choices);
            }

            //look for first non root AI node along this path and add it to cache
            if (!found && curr != root && curr.isAI()) {
                found = true;
                assert curr.isCached() || printPath(path);
                MCTSGameTree.addNode(CACHE, game, curr);
            }

            //there are unexplored children of node
            //assume we explore children of a node in increasing order of the choices
            if (curr.size() < choices.size()) {
                final int idx = curr.size();
                final Object[] choice = choices.get(idx);
                game.executeNextEvent(choice);
                final MCTSGameTree child = new MCTSGameTree(curr, idx, game.getScore());
                assert (child.desc = MCTSGameTree.obj2String(choice[0])).equals(child.desc);
                curr.addChild(child);
                path.add(child);
                return path;

            //all the children are in the tree, find the "best" child to explore
            } else {

                assert curr.size() == choices.size() : "ERROR! Different number of choices in node and game" +
                    printPath(path) + MCTSGameTree.printNode(curr, choices);

                MCTSGameTree next = null;
                double bestS = Double.NEGATIVE_INFINITY ;
                for (final MCTSGameTree child : curr) {
                    final double raw =
                            child.getAvg() * (curr.isAI() ? 1 : -1) +
                            MCTSAI.UCB1_C * Math.sqrt(Math.log(curr.getNumSim()) / child.getNumSim());
                    final double S = child.modify(raw);
                    if (S > bestS) {
                        bestS = S;
                        next = child;
                    }
                }

                //move down the tree
                curr = next;

                //update the game state and path
                game.executeNextEvent(choices.get(curr.getChoice()));
                path.add(curr);
            }
        }

        return path;
    }

    //returns a reward in the range [0, 1]
    private double randomPlay(final MCTSGameTree node, final MagicGame game) {
        //terminal node, no need for random play
        if (game.isFinished()) {
            if (game.getLosingPlayer() == game.getScorePlayer()) {
                node.setAILose(0);
                return 0.0;
            } else {
                node.setAIWin(0);
                return 1.0;
            }
        }

        if (!CHEAT) {
            game.showRandomizedHiddenCards();
        }
        final int startActions = game.getNumActions();
        getNextChoices(game, true);
        final int actions = Math.min(MAX_ACTIONS, game.getNumActions() - startActions);

        if (!game.isFinished()) {
            return 0.5;
        } else if (game.getLosingPlayer() == game.getScorePlayer()) {
            return actions/(2.0 * MAX_ACTIONS);
        } else {
            return 1.0 - actions/(2.0 * MAX_ACTIONS);
        }
    }

    private List<Object[]> getNextChoices(final MagicGame game, final boolean sim) {

        final int startActions = game.getNumActions();

        //use fast choices during simulation
        game.setFastChoices(sim);

        // simulate game until it is finished or simulated MAX_ACTIONS actions
        while (!game.isFinished() &&
               (game.getNumActions() - startActions) < MAX_ACTIONS) {

            //do not accumulate score down the tree when not in simulation
            if (!sim) {
                game.setScore(0);
            }

            if (!game.hasNextEvent()) {
                game.executePhase();
                continue;
            }

            //game has next event
            final MagicEvent event = game.getNextEvent();

            if (!event.hasChoice()) {
                game.executeNextEvent();
                continue;
            }

            //event has choice

            if (sim) {
                //get simulation choice and execute
                final Object[] choice = event.getSimulationChoiceResult(game);
                assert choice != null : "ERROR! No choice found during MCTS sim";
                game.executeNextEvent(choice);

                //terminate early if score > MIN_SCORE or score < -MIN_SCORE
                if (game.getScore() < -MIN_SCORE) {
                    game.setLosingPlayer(game.getScorePlayer());
                }
                if (game.getScore() > MIN_SCORE) {
                    game.setLosingPlayer(game.getScorePlayer().getOpponent());
                }
            } else {
                //get list of possible AI choices
                List<Object[]> choices = null;
                if (game.getNumActions() == 0) {
                    //map the RCHOICES to the current game instead of recomputing the choices
                    choices = new ArrayList<Object[]>(RCHOICES.size());
                    for (final Object[] choice : RCHOICES) {
                        choices.add(game.map(choice));
                    }
                } else {
                    choices = event.getArtificialChoiceResults(game);
                }
                assert choices != null;

                final int size = choices.size();
                assert size > 0 : "ERROR! No choice found during MCTS getACR";

                if (size == 1) {
                    //single choice
                    game.executeNextEvent(choices.get(0));
                } else {
                    //multiple choice
                    return choices;
                }
            }
        }

        //game is finished or number of actions > MAX_ACTIONS
        return Collections.emptyList();
    }

    private static String CR2String(final Object[] choiceResults) {
        final StringBuilder buffer=new StringBuilder();
        if (choiceResults!=null) {
            buffer.append(" (");
            boolean first=true;
            for (final Object choiceResult : choiceResults) {
                if (first) {
                    first=false;
                } else {
                    buffer.append(',');
                }
                buffer.append(choiceResult);
            }
            buffer.append(')');
        }
        return buffer.toString();
    }

    private boolean printPath(final List<MCTSGameTree> path) {
        final StringBuilder sb = new StringBuilder();
        for (final MCTSGameTree p : path) {
            sb.append(" -> ").append(p.desc);
        }
        log(sb.toString());
        return true;
    }
}
