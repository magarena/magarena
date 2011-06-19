package magic.ai;

import java.util.*;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicPhase;
import magic.model.event.MagicEvent;
import magic.model.choice.*;

/*
UCT algorithm from Kocsis and Sezepesvari 2006

function playOneSeq(root)
    nodes = [root]
    while (nodes.last is not leaf) do
      nodes append descendByUCB1(node.last)
    //assume value of leaf nodes are known
    //node.init is all elements except the last one
    updateValue(nodes.init, -nodes.last.value) 

function descendByUCB1(node)
    nb = sum of nb in node's children
    for each node n in node's children
        if n.nb = 0
          v[n] = infinity
        else
          v[n] = 1 - n.value/n.nb + sqrt(2 * log(nb) / n.nb)
    return n that maximizes v[n]

function updateValue(nodes, value)
    for each node n in nodes
        n.value += value
        n.nb += 1
        value = 1 - value

Modified UCT for MoGO in Wang and Gelly 2007

function playOneGame(state)
   create node root from current game state
   init tree to empty tree
   while there is time and memory
     //build the game tree one node at a time
     playOneSeqMC(root, tree)
   return descendByUCB1(root)

function playOneSeqMC(root, tree)
   nodes = [root]
   while (nodes.last is not in the tree)
     nodes append descendByUCB1(node.last)
   tree add nodes.last
   nodes.last.value = getValueByMC(nodes.last)
   updateValue(nodes.init, -nodes.last.value)

function getValueByMC(node) 
   play one random game starting from node
   return 1 if player 1 (max) wins, 0 if player 2 wins (min)
*/

//AI using Monte Carlo Tree Search
public class MCTSAI implements MagicAI {

    private final List<Integer> LENS = new LinkedList<Integer>();
    private final boolean LOGGING;
	private final boolean CHEAT;
    private static final int MAX_ACTIONS = 10000;
    
    //higher C -> more exploration less exploitation
    static final double C = 1.0;

    //boost score of win nodes by BOOST 
    static final int BOOST = 1000000;

    //cache nodes to reuse them in later decision
    private final NodeCache cache = new NodeCache(1000);

    public MCTSAI() {
        //no loggig, cheats
        this(false, true);
    }

    public MCTSAI(final boolean printLog, final boolean cheat) {
        LOGGING = printLog || (System.getProperty("debug") != null);
        CHEAT = cheat;
    }
    
    static public int obj2StringHash(Object obj) {
        return obj2String(obj).hashCode();
    }

    static public String obj2String(Object obj) {
        if (obj == null) {
            return "null";
        } else if (obj instanceof MagicBuilderPayManaCostResult) {
            return ((MagicBuilderPayManaCostResult)obj).getText();
        } else {
            return obj.toString();
        }
    }

    private void addNode(final MagicGame game, final MCTSGameTree node) {
        final long gid = game.getGameId();
        cache.put(gid, node);
        node.setCached();
        System.err.println("ADDED: " + game.getIdString());
    }

    private MCTSGameTree getNode(final MagicGame game, List<Object[]> rootChoices) {
        final long gid = game.getGameId();
        MCTSGameTree candidate = cache.get(gid);
        
        if (candidate != null) { 
            System.err.println("CACHE HIT");
            System.err.println("HIT  : " + game.getIdString());
            printNode(candidate, rootChoices);
            return candidate;
        } else {
            System.err.println("CACHE MISS");
            System.err.println("MISS : " + game.getIdString());
            printNode(candidate, rootChoices);
            return new MCTSGameTree(-1, -1, -1);
        }
    }

    private void log(final String message) {
        if (LOGGING) {
            System.err.println(message);
        }
    }

    private double UCT(final MCTSGameTree parent, final MCTSGameTree child) {
        return (parent.isAI() ? 1.0 : -1.0) * child.getV() + 
            C * Math.sqrt(Math.log(parent.getNumSim()) / child.getNumSim());
    }

    public synchronized Object[] findNextEventChoiceResults(
            final MagicGame startGame, 
            final MagicPlayer scorePlayer) {

        final MagicGame choiceGame = new MagicGame(startGame, scorePlayer);
        final MagicEvent event = choiceGame.getNextEvent();
        final List<Object[]> rootChoices = event.getArtificialChoiceResults(choiceGame);

        final int size = rootChoices.size();
        final String pinfo = "MCTS " + scorePlayer.getIndex() + " (" + scorePlayer.getLife() + ")";
        
        // No choice results
        assert size > 0 : "ERROR! MCTS: no choice found at start";
    
        // Single choice result
        if (size == 1) {
            return startGame.map(rootChoices.get(0));
        }
        
        //ArtificialLevel = number of seconds to run MCTSAI
        //debugging: max time is 1 billion, max sim is 500
        //normal   : max time is 1000 * str, max sim is 1 billion
        final int MAXTIME = System.getProperty("debug") != null ?
            1000000000 : 1000 * startGame.getArtificialLevel(scorePlayer.getIndex());
        final int MAXSIM = System.getProperty("debug") != null ? 
            500 : 1000000000;
        final long STARTTIME = System.currentTimeMillis();
       
        //root represents the start state
        //final MCTSGameTree root = new MCTSGameTree(-1, -1, -1);
        final MCTSGameTree root = getNode(startGame, rootChoices);
        root.desc = "root";
        LENS.clear();

        //end simulations once root is solved or time is up
        int sims = 0;
        for (; System.currentTimeMillis() - STARTTIME < MAXTIME &&
               sims < MAXSIM && 
               !root.isAIWin(); sims++) {
            //clone the MagicGame object for simulation
            final MagicGame rootGame = new MagicGame(startGame, scorePlayer);
            if (!CHEAT) {
                rootGame.setKnownCards();
            }
            
            //pass in a clone of the state, genNewTreeNode grows the tree by one node
            //and returns the path from the root to the new node
            final LinkedList<MCTSGameTree> path = growTree(root, rootGame);
          
            assert path.size() >= 2 : "ERROR! MCTS: length of path is " + path.size();

            // play a simulated game to get score
            // update all nodes along the path from root to new node 
            final double score = randomPlay(path.getLast(), rootGame);
            
            for (MCTSGameTree child = null, parent = null; 
                 !path.isEmpty(); 
                 child = parent) {
                
                parent = path.removeLast(); 
                parent.updateScore(score);

                //update game theoretic value
                if (child != null && child.isSolved()) {
                    if (parent.isAI() && child.isAIWin()) {
                        parent.setAIWin();
                    } else if (parent.isAI() && child.isAILose()) {
                        parent.incLose();
                    } else if (!parent.isAI() && child.isAIWin()) {
                        parent.incLose();
                    } else if (!parent.isAI() && child.isAILose()) {
                        parent.setAILose();
                    }
                }
            }
        }

        assert root.size() > 0 : "ERROR! MCTS: root has no children but there are " + size + " choices";

        //select the best choice (child that has the highest secure score)
        final MCTSGameTree first = root.first();
        double maxR = first.getRank();
        int bestC = first.getChoice();
        for (MCTSGameTree node : root) {
            final double R = node.getRank();
            final int C = node.getChoice();
            if (R > maxR) { 
                maxR = R;
                bestC = C;
            }
        }
        final Object[] selected = rootChoices.get(bestC); 

        if (LOGGING) {
            final long duration = System.currentTimeMillis() - STARTTIME;
            int minL = 1000000;
            int maxL = -1;
            int sumL = 0;
            for (int len : LENS) {
                sumL += len;
                if (len > maxL) maxL = len;
                if (len < minL) minL = len;
            }
            log("MCTS:\ttime: " + duration + 
                     "\tsims: " + (root.getNumSim() - sims) + "+" + sims +
                     "\tmin: " + minL + 
                     "\tmax: " + maxL + 
                     "\tavg: " + (sumL / (LENS.size()+1)));
            log(pinfo);
            for (MCTSGameTree node : root) {
                final StringBuffer out = new StringBuffer();
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
                } else if (node.isAILose()) {
                    out.append("lose");
                } else {
                    out.append("?");
                }
                out.append(']');
                out.append(CR2String(rootChoices.get(node.getChoice())));
                log(out.toString());
            }
        }

        return startGame.map(selected);
    }

    private static String CR2String(Object[] choiceResults) {
		final StringBuffer buffer=new StringBuffer();
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
               
    private boolean checkNode(final MCTSGameTree curr, List<Object[]> choices) {
        for (int i = 0; i < choices.size(); i++) {
            final String checkStr = obj2String(choices.get(i)[0]);
            if (!curr.choicesStr[i].equals(checkStr)) {
                return false;
            }
        }
        for (MCTSGameTree child : curr) {
            final String checkStr = obj2String(choices.get(child.getChoice())[0]);
            if (!child.desc.equals(checkStr)) {
                return false;
            }
        }
        return true;
    }
                    
    private static String printNode(final MCTSGameTree curr, List<Object[]> choices) {
        if (curr == null) {
            return "NODE is null";
        }
        if (curr.choicesStr != null) {
            for (String str : curr.choicesStr) {
                System.err.println("PAREN: " + str);
            }
        } else {
            System.err.println("PAREN: not defined");
        }
        for (MCTSGameTree child : curr) {
            System.err.println("CHILD: " + child.desc);
        }
        for (Object[] choice : choices) {
            final int checksum = obj2StringHash(choice[0]);
            System.err.println("GAME : " + obj2String(choice[0]));
        }
        return "";
    }

    public static String printPath(final List<MCTSGameTree> path) {
        for (MCTSGameTree p : path) {
            System.err.print(" -> " + p.desc);
        }
        System.err.println();
        return "";
    }

    private LinkedList<MCTSGameTree> growTree(final MCTSGameTree root, final MagicGame game) {
        final LinkedList<MCTSGameTree> path = new LinkedList<MCTSGameTree>();
        boolean found = false;
        MCTSGameTree curr = root;
        path.add(curr);

        for (List<Object[]> choices = getNextChoices(game, curr == root, false);
             choices != null;
             choices = getNextChoices(game, curr == root, false)) {

            assert choices.size() > 0 : "ERROR! No choice at start of genNewTreeNode";
            
            assert !curr.hasDetails() || curr.getMaxChildren() == choices.size() : 
                "ERROR! Capacity of node is " + curr.getMaxChildren() + ", number of choices is " + choices.size() + 
                "\n" + game + printPath(path) + printNode(curr, choices);
            
            assert !curr.hasDetails() || checkNode(curr, choices) : 
                "ERROR! Inconsistent node found" + "\n" + game +
                printPath(path) + printNode(curr, choices);
          
            final MagicEvent event = game.getNextEvent();
           
            //first time considering the choices available at this node
            if (!curr.hasDetails()) {
                curr.setIsAI(game.getScorePlayer() == event.getPlayer());
                curr.setMaxChildren(choices.size());
                curr.setChoicesStr(choices);
            }

            //look for first non root AI node along this path and add it to cache
            if (!found && curr != root && curr.isAI()) {
                found = true;
                if (!curr.isCached()) {
                    printPath(path);
                    addNode(game, curr);
                }
            }

            //there are unexplored children of node
            //assume we explore children of a node in increasing order of the choices
            if (curr.size() < choices.size()) {
                Object[] choice = choices.get(curr.size());
                game.executeNextEvent(choice);
                final MCTSGameTree child = new MCTSGameTree(
                        curr.size(), 
                        game.getScore(), 
                        obj2StringHash(choice[0]));
                child.desc = obj2String(choice[0]);
                curr.addChild(child);
                path.add(child);
                return path;
            
            //all the children are in the tree, find the "best" child to explore
            } else {

                assert curr.size() == choices.size() : "ERROR! Different number of choices in node and game" + 
                    printPath(path) + printNode(curr, choices); 

                MCTSGameTree next = null;
                double bestV = Double.NEGATIVE_INFINITY;
                for (MCTSGameTree child : curr) {
                    //skip won nodes
                    if (child.isAIWin()) {
                        continue;
                    }
                    final double v = UCT(curr, child);
                    if (v > bestV) {
                        bestV = v;
                        next = child;
                    }
                }

                //move down the tree
                curr = next;
                
                game.executeNextEvent(choices.get(curr.getChoice()));
                path.add(curr);
            }
        } 
       
        return path;
    }

    private double randomPlay(final MCTSGameTree node, final MagicGame game) {
        //terminal node, no need for random play
        if (game.isFinished()) {
            assert game.getLosingPlayer() != null : "ERROR! game finished but no losing player";
            
            if (game.getLosingPlayer() == game.getScorePlayer()) {
                node.setAILose();
                return -1.0;
            } else {
                node.setAIWin();
                return 1.0;
            }
        }

        final int startActions = game.getNumActions();
        getNextChoices(game, false, true);
        final int actions = game.getNumActions() - startActions;
        
        if (LOGGING) {
            LENS.add(actions);
        }

        if (game.getLosingPlayer() == null) {
            return 0;
        } else if (game.getLosingPlayer() == game.getScorePlayer()) {
            return  -(1.0 - actions/((double)MAX_ACTIONS));
        } else {
            return  1.0 - actions/((double)MAX_ACTIONS);
        }
    }
    
    private List<Object[]> getNextChoices(
            final MagicGame game, 
            final boolean isRoot, 
            final boolean sim) {
        
        final int startActions = game.getNumActions();
        
        // simulate game until it is finished or simulated MAX_ACTIONS actions
        while (!game.isFinished() && (game.getNumActions() - startActions) < MAX_ACTIONS) {
            //do not accumulate score down the tree
            game.setScore(0);

            if (!game.hasNextEvent()) {
                game.getPhase().executePhase(game);
                continue;
            }

            //game has next event
            final MagicEvent event = game.getNextEvent();

            if (!event.hasChoice()) {
                game.executeNextEvent(MagicEvent.NO_CHOICE_RESULTS);
                continue;
            }
            
            //event has choice

            if (sim) {
                //get simulation choice and execute
                final Object[] choice = event.getSimulationChoiceResult(game);
                assert choice != null : "ERROR! MCTS: no choice found during sim";
                game.executeNextEvent(choice);
            } else {
                //get list of possible AI choices
                final List<Object[]> choices = event.getArtificialChoiceResults(game);
                final int size = choices.size();
                assert size > 0 : "ERROR! MCTS: no choice found";
                if (size == 1) {
                    //single choice
                    game.executeNextEvent(choices.get(0));
                } else {
                    //multiple choice
                    return choices;
                }
            }
        }
        
        //game is finished
        return null;
    }
}

//only store one copy of MagicGame
//each tree node stores the choice from the parent that leads to this node
//so we only need one copy of MagicGame for MCTSAI
class MCTSGameTree implements Iterable<MCTSGameTree> {
    
    private final LinkedList<MCTSGameTree> children = new LinkedList<MCTSGameTree>();
    private final int choice;
    private final int checksum;
    private boolean isAI;
    private boolean isCached = false;
    private int maxChildren = -1;
    private int numLose = 0;
    private int numSim = 0;
    private int evalScore = 0;
    private double score = 0;
    public String desc;
    public String[] choicesStr;

    public MCTSGameTree(final int choice, final int evalScore, final int checksum) { 
        this.evalScore = evalScore;
        this.choice = choice;
        this.checksum = checksum;
    }

    public boolean isCached() {
        return isCached;
    }

    public void setCached() {
        isCached = true;
    }

    public boolean hasDetails() {
        return maxChildren != -1;
    }

    public int getChecksum() {
        return checksum;
    }

    public void setChoicesStr(List<Object[]> choices) {
        choicesStr = new String[choices.size()];
        for (int i = 0; i < choices.size(); i++) {
            choicesStr[i] = MCTSAI.obj2String(choices.get(i)[0]);
        }
    }

    public void setMaxChildren(final int mc) {
        maxChildren = mc;
    }
    
    public int getMaxChildren() {
        return maxChildren;
    }

    public boolean isAI() {
        return isAI;
    }
    
    public void setIsAI(final boolean ai) {
        this.isAI = ai;
    }

    public boolean isSolved() {
        return evalScore == Integer.MAX_VALUE || evalScore == Integer.MIN_VALUE;
    }
    
    public void updateScore(final double score) {
        this.score += score;
        numSim += 1;
    }

    public boolean isAIWin() {
        return evalScore == Integer.MAX_VALUE;
    }
    
    public boolean isAILose() {
        return evalScore == Integer.MIN_VALUE;
    }

    public void incLose() {
        numLose++;
        if (numLose == maxChildren) {
            if (isAI) {
                setAILose();
            } else {
                setAIWin();
            }
        }
    }

    public int getChoice() {
        return choice;
    }

    public void setAIWin() {
        evalScore = Integer.MAX_VALUE;
    }

    public void setAILose() {
        evalScore = Integer.MIN_VALUE;
    }

    public int getEvalScore() {
        return evalScore;
    }

    public double getScore() {
        return score;
    }

    public double getRank() {
        if (isAIWin()) {
            return MCTSAI.BOOST + getNumSim();
        } else if (isAILose()) {
            return getNumSim();
        } else {
            return getNumSim();
        }
    }
    
    public int getNumSim() {
        return numSim;
    }

    public double getV() {
        return score / numSim;
    }
    
    public double getSecureScore() {
        return getV() + 1.0/Math.sqrt(getNumSim());
    }

    public void addChild(MCTSGameTree child) {
        assert children.size() < maxChildren : "ERROR! Number of children nodes exceed maxChildren";
        children.add(child);
    }
    
    public void removeLast() {
        children.removeLast();
    }
    
    public MCTSGameTree first() {
        return children.get(0);
    }
    
    public Iterator<MCTSGameTree> iterator() {
        return children.iterator();
    }

    public int size() {
        return children.size();
    }
}

class NodeCache extends LinkedHashMap<Long, MCTSGameTree> {
	private static final long serialVersionUID = 1L;
    private final int capacity;
    public NodeCache(int capacity) {
        super(capacity + 1, 1.1f, true);
        this.capacity = capacity;
    }

    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > capacity;
    }
}
