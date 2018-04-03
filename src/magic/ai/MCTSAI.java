package magic.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import magic.data.LRUCache;
import magic.exception.GameException;
import magic.model.MagicGame;
import magic.model.MagicGameLog;
import magic.model.MagicPlayer;
import magic.model.choice.MagicBuilderPayManaCostResult;
import magic.model.event.MagicEvent;

/*
AI using Monte Carlo Tree Search

Classical MCTS (UCT)
 - use UCB1 formula for selection with C = sqrt(2)
 - reward either 0 or 1
 - backup by averaging
 - uniform random simulated playout
 - score = XX% (25000 matches against MMAB-1)

Enhancements to basic UCT
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
  eps-greedy is not consistent for fixed eps (with prob eps select randomly, else use score)
  eps-greedy is consistent but not frugal if eps dynamically decreases to 0
  UCB1 is consistent but not frugal
  score = average is not consistent
  score = (total reward + K)/(total simulation + 2K) is consistent and frugal!
  using v_t threshold ensures consistency for case of reward in {0,1} using any score function
    v(s) < v_t (0.3), randomly pick a child, else pick child that maximize score

Monte-Carlo Tree Search in Lines of Action
  1-ply lookahead to detect direct win for player to move
  secure child formula for decision v + A/sqrt(n)
  evaluation cut-off: use score function to stop simulation early
  use evaluation score to remove "bad" moves during simulation
  use evaluation score to keep k-best moves
  mixed: start with corrective, rest of the moves use greedy
*/
public class MCTSAI extends MagicAI {

    private static int MIN_SCORE = Integer.MAX_VALUE;
    static int MIN_SIM = Integer.MAX_VALUE;
    private static final int MAX_CHOICES = 1000;
    static double UCB1_C = 0.4;
    static double RATIO_K = 1.0;
    private int sims = 0;

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

    private final boolean CHEAT;

    //cache nodes to reuse them in later decision
    private final LRUCache<Long, MCTSGameTree> CACHE = new LRUCache<Long, MCTSGameTree>(1000);

    public MCTSAI(final boolean cheat) {
        CHEAT = cheat;
    }

    private void log(final String message) {
        MagicGameLog.log(message);
    }

    @Override
    public Object[] findNextEventChoiceResults(final MagicGame startGame, final MagicPlayer scorePlayer) {

        // Determine possible choices
        final MagicGame aiGame = new MagicGame(startGame, scorePlayer);
        if (!CHEAT) {
            aiGame.hideHiddenCards();
        }
        final MagicEvent event = aiGame.getNextEvent();
        final List<Object[]> RCHOICES = event.getArtificialChoiceResults(aiGame);

        final int size = RCHOICES.size();

        // No choice
        assert size > 0 : "ERROR! No choice found at start of MCTS";

        // Single choice
        if (size == 1) {
            return startGame.map(RCHOICES.get(0));
        }

        //root represents the start state
        final MCTSGameTree root = MCTSGameTree.getNode(CACHE, aiGame, RCHOICES);

        log("MCTS cached=" + root.getNumSim());

        sims = 0;
        final ExecutorService executor = Executors.newFixedThreadPool(getMaxThreads());
        final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

        // ensure tree update runs at least once
        final int aiLevel = scorePlayer.getAiProfile().getAiLevel();
        final long START_TIME = System.currentTimeMillis();
        final long END_TIME = START_TIME + 1000 * aiLevel;
        final Runnable updateTask = new Runnable() {
            @Override
            public void run() {
                TreeUpdate(this, root, aiGame, executor, queue, END_TIME, RCHOICES);
            }
        };

        updateTask.run();

        try {
            // wait for artificialLevel + 1 seconds for jobs to finish
            executor.awaitTermination(aiLevel + 1, TimeUnit.SECONDS);
        } catch (final InterruptedException ex) {
            throw new RuntimeException(ex);
        } finally {
            // force termination of workers
            executor.shutdownNow();
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

        log(outputChoice(scorePlayer, root, START_TIME, bestC, sims, RCHOICES));

        return startGame.map(RCHOICES.get(bestC));
    }

    private Runnable genSimulationTask(final MagicGame rootGame, final LinkedList<MCTSGameTree> path, final BlockingQueue<Runnable> queue) {
        return new Runnable() {
            @Override
            public void run() {
                // propagate result of random play up the path
                final double score = randomPlay(path.getLast(), rootGame);
                queue.offer(genBackpropagationTask(score, path));
            }
        };
    }

    private Runnable genBackpropagationTask(final double score, final LinkedList<MCTSGameTree> path) {
        return new Runnable() {
            @Override
            public void run() {
                final Iterator<MCTSGameTree> iter = path.descendingIterator();
                MCTSGameTree child = null;
                MCTSGameTree parent = null;
                while (iter.hasNext()) {
                    child = parent;
                    parent = iter.next();

                    parent.removeVirtualLoss();
                    parent.updateScore(child, score);
                }
            }
        };
    }

    public void TreeUpdate(
        final Runnable updateTask,
        final MCTSGameTree root,
        final MagicGame aiGame,
        final ExecutorService executor,
        final BlockingQueue<Runnable> queue,
        final long END_TIME,
        final List<Object[]> RCHOICES
    ) {

        //prioritize backpropagation tasks
        while (queue.isEmpty() == false) {
            try {
                queue.take().run();
            } catch (InterruptedException e) {
                // occurs when shutdownNow is invoked
                return;
            }
        }

        sims++;

        //clone the MagicGame object for simulation
        final MagicGame rootGame = new MagicGame(aiGame, aiGame.getScorePlayer());

        //pass in a clone of the state,
        //genNewTreeNode grows the tree by one node
        //and returns the path from the root to the new node
        final LinkedList<MCTSGameTree> path = growTree(root, rootGame, RCHOICES);

        assert path.size() >= 2 : "ERROR! length of MCTS path is " + path.size();

        // play a simulated game to get score
        // update all nodes along the path from root to new node

        final boolean running = System.currentTimeMillis() < END_TIME;

        // submit random play to executor
        if (running) {
            try {
                executor.execute(genSimulationTask(rootGame, path, queue));
            } catch (RejectedExecutionException e) {
                // occurs when trying to submit to a execute that has shutdown
                return;
            }
        }

        // virtual loss + game theoretic value propagation
        final Iterator<MCTSGameTree> iter = path.descendingIterator();
        MCTSGameTree child = null;
        MCTSGameTree parent = null;
        while (iter.hasNext()) {
            child = parent;
            parent = iter.next();

            parent.recordVirtualLoss();

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

        // end simulations once root is AI win or time is up
        if (running && root.isAIWin() == false) {
            try {
                executor.execute(updateTask);
            } catch (RejectedExecutionException e) {
                // occurs when trying to submit to a execute that has shutdown
                return;
            }
        } else {
            executor.shutdown();
        }
    }

    private String outputChoice(
        final MagicPlayer scorePlayer,
        final MCTSGameTree root,
        final long START_TIME,
        final int bestC,
        final int sims,
        final List<Object[]> RCHOICES
    ) {

        final StringBuilder out = new StringBuilder();
        final long duration = System.currentTimeMillis() - START_TIME;

        out.append("MCTS" +
                   " cheat=" + CHEAT +
                   " index=" + scorePlayer.getIndex() +
                   " life=" + scorePlayer.getLife() +
                   " turn=" + scorePlayer.getGame().getTurn() +
                   " phase=" + scorePlayer.getGame().getPhase().getType() +
                   " sims=" + sims +
                   " time=" + duration);
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

    private LinkedList<MCTSGameTree> growTree(final MCTSGameTree root, final MagicGame game, final List<Object[]> RCHOICES) {
        final LinkedList<MCTSGameTree> path = new LinkedList<MCTSGameTree>();
        boolean found = false;
        MCTSGameTree curr = root;
        path.add(curr);

        for (List<Object[]> choices = getNextChoices(game, RCHOICES);
             !choices.isEmpty() && !Thread.currentThread().isInterrupted();
             choices = getNextChoices(game, RCHOICES)) {

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
                //assert curr.isCached() || printPath(path);
                MCTSGameTree.addNode(CACHE, game, curr);
            }

            //there are unexplored children of node
            //assume we explore children of a node in increasing order of the choices
            if (curr.size() < choices.size()) {
                final int idx = curr.size();
                final Object[] choice = choices.get(idx);
                final String choiceStr = MCTSGameTree.obj2String(choice[0]);
                game.executeNextEvent(choice);
                final MCTSGameTree child = new MCTSGameTree(curr, idx, game.getScore());
                assert (child.desc = choiceStr).equals(child.desc);
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
                    final double raw = child.getUCT();
                    final double S = child.modify(raw);
                    if (S > bestS) {
                        bestS = S;
                        next = child;
                    }
                }

                //move down the tree
                curr = next;

                //update the game state and path
                try {
                    game.executeNextEvent(choices.get(curr.getChoice()));
                } catch (final IndexOutOfBoundsException ex) {
                    printPath(path);
                    MCTSGameTree.printNode(curr, choices);
                    throw new GameException(ex, game);
                }
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
        final int[] counts = runSimulation(game);

        //System.err.println("COUNTS:\t" + counts[0] + "\t" + counts[1]);

        if (!game.isFinished()) {
            return 0.5;
        } else if (game.getLosingPlayer() == game.getScorePlayer()) {
            // bias losing simulations towards ones where opponent makes more choices
            return counts[1] / (2.0 * MAX_CHOICES);
        } else {
            // bias winning simulations towards ones where AI makes less choices
            return 1.0 - counts[0] / (2.0 * MAX_CHOICES);
        }
    }

    private int[] runSimulation(final MagicGame game) {

        int aiChoices = 0;
        int oppChoices = 0;

        //use fast choices during simulation
        game.setFastChoices(true);

        // simulate game until it is finished or reached MAX_CHOICES
        while (aiChoices < MAX_CHOICES &&
               oppChoices < MAX_CHOICES &&
               !Thread.currentThread().isInterrupted() &&
               game.advanceToNextEventWithChoice()) {
            final MagicEvent event = game.getNextEvent();

            if (event.getPlayer() == game.getScorePlayer()) {
                aiChoices++;
            } else {
                oppChoices++;
            }

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
        }

        //game is finished or reached MAX_CHOICES
        return new int[]{aiChoices, oppChoices};
    }

    private List<Object[]> getNextChoices(final MagicGame game, final List<Object[]> RCHOICES) {
        //disable fast choices
        game.setFastChoices(false);

        while (game.advanceToNextEventWithChoice()) {

            //do not accumulate score down the tree when not in simulation
            game.setScore(0);

            final MagicEvent event = game.getNextEvent();

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

        //game is finished
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

//each tree node stores the choice from the parent that leads to this node
class MCTSGameTree implements Iterable<MCTSGameTree> {

    private final MCTSGameTree parent;
    private final LinkedList<MCTSGameTree> children = new LinkedList<MCTSGameTree>();
    private final int choice;
    private boolean isAI;
    private boolean isCached;
    private int maxChildren = -1;
    private int numLose;
    private int numSim;
    private int evalScore;
    private int steps;
    private double sum;
    private double S;
    String desc;
    private String[] choicesStr;

    //min sim for using robust max
    private int maxChildSim = MCTSAI.MIN_SIM;

    MCTSGameTree(final MCTSGameTree parent, final int choice, final int evalScore) {
        this.evalScore = evalScore;
        this.choice = choice;
        this.parent = parent;
    }

    private static boolean log(final String message) {
        MagicGameLog.log(message);
        return true;
    }

    static String obj2String(final Object obj) {
        if (obj == null) {
            return "null";
        } else if (obj instanceof MagicBuilderPayManaCostResult) {
            return ((MagicBuilderPayManaCostResult)obj).getText();
        } else {
            return obj.toString();
        }
    }

    static void addNode(final LRUCache<Long, MCTSGameTree> cache, final MagicGame game, final MCTSGameTree node) {
        if (node.isCached()) {
            return;
        }
        final long gid = game.getStateId();
        cache.put(gid, node);
        node.setCached();
        assert log("ADDED: " + game.getIdString());
    }

    static MCTSGameTree getNode(final LRUCache<Long, MCTSGameTree> cache, final MagicGame game, final List<Object[]> choices) {
        final long gid = game.getStateId();
        final MCTSGameTree candidate = cache.get(gid);

        if (candidate != null) {
            assert log("CACHE HIT");
            assert log("HIT  : " + game.getIdString());
            //assert printNode(candidate, choices);
            return candidate;
        } else {
            assert log("CACHE MISS");
            assert log("MISS : " + game.getIdString());
            final MCTSGameTree root = new MCTSGameTree(null, -1, -1);
            assert (root.desc = "root").equals(root.desc);
            return root;
        }
    }

    static boolean checkNode(final MCTSGameTree curr, final List<Object[]> choices) {
        if (curr.getMaxChildren() != choices.size()) {
            return false;
        }
        for (int i = 0; i < choices.size(); i++) {
            final String checkStr = obj2String(choices.get(i)[0]);
            if (!curr.choicesStr[i].equals(checkStr)) {
                return false;
            }
        }
        for (final MCTSGameTree child : curr) {
            final String checkStr = obj2String(choices.get(child.getChoice())[0]);
            if (!child.desc.equals(checkStr)) {
                return false;
            }
        }
        return true;
    }


    static boolean printNode(final MCTSGameTree curr, final List<Object[]> choices) {
        if (curr.choicesStr != null) {
            for (final String str : curr.choicesStr) {
                log("PAREN: " + str);
            }
        } else {
            log("PAREN: not defined");
        }
        for (final MCTSGameTree child : curr) {
            log("CHILD: " + child.desc);
        }
        for (final Object[] choice : choices) {
            log("GAME : " + obj2String(choice[0]));
        }
        return true;
    }


    boolean isCached() {
        return isCached;
    }

    private void setCached() {
        isCached = true;
    }

    boolean hasDetails() {
        return maxChildren != -1;
    }

    boolean setChoicesStr(final List<Object[]> choices) {
        choicesStr = new String[choices.size()];
        for (int i = 0; i < choices.size(); i++) {
            choicesStr[i] = obj2String(choices.get(i)[0]);
        }
        return true;
    }

    void setMaxChildren(final int mc) {
        maxChildren = mc;
    }

    private int getMaxChildren() {
        return maxChildren;
    }

    boolean isAI() {
        return isAI;
    }

    boolean isOpp() {
        return !isAI;
    }

    void setIsAI(final boolean ai) {
        this.isAI = ai;
    }

    boolean isSolved() {
        return evalScore == Integer.MAX_VALUE || evalScore == Integer.MIN_VALUE;
    }

    void recordVirtualLoss() {
        numSim++;
    }

    void removeVirtualLoss() {
        numSim--;
    }

    void updateScore(final MCTSGameTree child, final double delta) {
        final double oldMean = (numSim > 0) ? sum/numSim : 0;
        sum += delta;
        numSim += 1;
        final double newMean = sum/numSim;
        S += (delta - oldMean) * (delta - newMean);

        //if child has sufficient simulations, backup using robust max instead of average
        if (child != null && child.getNumSim() > maxChildSim) {
            maxChildSim = child.getNumSim();
            sum = child.sum;
            numSim = child.numSim;
        }
    }

    double getUCT() {
        return getV() + MCTSAI.UCB1_C * Math.sqrt(Math.log(parent.getNumSim()) / getNumSim());
    }

    //decrease score of lose node, boost score of win nodes
    double modify(final double sc) {
        if ((!parent.isAI() && isAIWin()) || (parent.isAI() && isAILose())) {
            return sc - 2.0;
        } else if ((parent.isAI() && isAIWin()) || (!parent.isAI() && isAILose())) {
            return sc + 2.0;
        } else {
            return sc;
        }
    }

    boolean isAIWin() {
        return evalScore == Integer.MAX_VALUE;
    }

    boolean isAILose() {
        return evalScore == Integer.MIN_VALUE;
    }

    void incLose(final int lsteps) {
        numLose++;
        steps = Math.max(steps, lsteps);
        if (numLose == maxChildren) {
            if (isAI) {
                setAILose(steps);
            } else {
                setAIWin(steps);
            }
        }
    }

    int getChoice() {
        return choice;
    }

    int getSteps() {
        return steps;
    }

    void setAIWin(final int aSteps) {
        evalScore = Integer.MAX_VALUE;
        steps = aSteps;
    }

    void setAILose(final int aSteps) {
        evalScore = Integer.MIN_VALUE;
        steps = aSteps;
    }

    double getDecision() {
        //boost decision score of win nodes by BOOST
        final int BOOST = 1000000;
        if (isAIWin()) {
            return BOOST + getNumSim();
        } else if (isAILose()) {
            return getNumSim();
        } else {
            return getNumSim();
        }
    }

    int getNumSim() {
        return numSim;
    }

    private double getSum() {
        // AI is max player, other is min player
        return parent.isAI() ? sum : -sum;
    }

    public double getAvg() {
        return sum / numSim;
    }

    double getV() {
        return getSum() / numSim;
    }

    void addChild(final MCTSGameTree child) {
        assert children.size() < maxChildren : "ERROR! Number of children nodes exceed maxChildren";
        children.add(child);
    }

    MCTSGameTree first() {
        return children.get(0);
    }

    @Override
    public Iterator<MCTSGameTree> iterator() {
        return children.iterator();
    }

    int size() {
        return children.size();
    }
}

