package magic.ai;

import java.util.*;

import magic.model.MagicGame;
import magic.model.phase.MagicPhase;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.MagicRandom;

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
    private int MAXTIME;
    private long STARTTIME;
    private static final int MAXEVENTS = 1000;
    
    //higher C -> more exploration less exploitation
    static final double C = 1.0;

    //boost score of win nodes by BOOST and decrease scores of lost nodes by BOOST
    static final int BOOST = 1000000;

    public MCTSAI() {
        this(false, true);
    }

    public MCTSAI(boolean printLog, boolean cheat) {
        LOGGING = printLog || (System.getProperty("debug") != null);
        CHEAT = cheat;
    }

    private void log(final String message) {
        if (LOGGING) {
            System.err.println(message);
        }
    }
    
    private void logi(final int num) {
        if (LOGGING) {
            System.err.print(num);
        }
    }
    
    private void logc(final char message) {
        if (LOGGING) {
            System.err.print(message);
        }
    }

    private double UCT(final MCTSGameTree parent, final MCTSGameTree child) {
        return (parent.isAI() ? 1.0 : -1.0) * child.getV() + 
            C * Math.sqrt(Math.log(parent.getNumSim()) / child.getNumSim());
    }

    public synchronized Object[] findNextEventChoiceResults(
            final MagicGame startGame, 
            final MagicPlayer scorePlayer) {
        //ArtificialLevel = number of seconds to run MCTSAI
        MAXTIME = 1000 * startGame.getArtificialLevel();
        STARTTIME = System.currentTimeMillis();
   
        final String pinfo = "MCTS " + scorePlayer.getIndex() + " (" + scorePlayer.getLife() + ")";
        final List<Object[]> choices = getCR(startGame, scorePlayer);
        final int size = choices.size();
        
        // No choice results
        if (size == 0) {
            log(pinfo);
            System.err.println("ERROR! MCTS: no choice found at start");
            return null;
        }
    
        // Single choice result
        if (size == 1) {
            return startGame.map(choices.get(0));
        }
        
        // repeat a number of simulations
        // each simulation does the following
        //   selects a path down the game tree and create a new leaf
        //   score the leaf by doing a random play to the end of the game
        //   update the score of all the ancestors of the leaf
        // return the "best" choice
       
        //root represents the start state
        final MCTSGameTree root = new MCTSGameTree(-1,-1);
        LENS.clear();

        //end simulations once root is solved or time is up
        for (; System.currentTimeMillis() - STARTTIME < MAXTIME && !root.isSolved(); ) {
            //create a new MagicGame for simulation
            final MagicGame rootGame = new MagicGame(startGame, scorePlayer);
            if (!CHEAT) {
                rootGame.setKnownCards();
            }
            
            //pass in a clone of the state, genNewTreeNode grows the tree by one node
            //and returns the path from the root to the new node
            final LinkedList<MCTSGameTree> path = genNewTreeNode(root, rootGame);
           
            if (path.size() < 2) {
                System.err.println("ERROR! MCTS: length of path is " + path.size());
                System.exit(1);
            }

            // play a simulated game to get score
            // update all nodes along the path from root to new node 
            final double score = randomPlay(path.getLast(), rootGame);
            //logc((score > 0.0) ? '.' : 'X');
            
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
        //logc('\n');

        if (root.size() == 0) {
            System.err.println("ERROR! MCTS: root has no children but there are " + size + " choices");
            System.exit(1);
        }

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
        final Object[] selected = choices.get(bestC); 

        if (LOGGING) {
            final long duration = System.currentTimeMillis() - STARTTIME;
            log("MCTS:  time: " + duration + "  sims:  " + root.getNumSim());
            int minL = 1000000;
            int maxL = -1;
            int sumL = 0;
            for (int len : LENS) {
                sumL += len;
                if (len > maxL) maxL = len;
                if (len < minL) minL = len;
            }
            log("min: " + minL + "  max: " + maxL + "  avg: " + (sumL / LENS.size()));
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
                out.append(CR2String(choices.get(node.getChoice())));
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

    private List<Object[]> getCR(final MagicGame game, final MagicPlayer player) {
        final MagicGame choiceGame = new MagicGame(game, player);
        final MagicEvent event = choiceGame.getNextEvent();
        return event.getArtificialChoiceResults(choiceGame);
    }

    // p is parent of n
    // n.nb is how many times the node n is simulated
    // sum of nb in all children of parent of n (same as p.nb)
    // select node n (child of node) that maximize v[n]
    // where v[n] = 1 - n.value/n.nb + C * sqrt(log(nb) / n.nb)
    // find a path from root to an unexplored node
    private LinkedList<MCTSGameTree> genNewTreeNode(final MCTSGameTree root, final MagicGame game) {
        final LinkedList<MCTSGameTree> path = new LinkedList<MCTSGameTree>();
        MCTSGameTree curr = root;
        path.add(curr);

        for (List<Object[]> choices = getNextMultiChoiceEvent(game, curr != root, false);
             choices != null;
             choices = getNextMultiChoiceEvent(game, curr != root, false)) {
          
            final MagicEvent event = game.getNextEvent();
            curr.setIsAI(game.getScorePlayer() == event.getPlayer());
            curr.setMaxChildren(choices.size());

            if (curr.size() < choices.size()) {
                //there are unexplored children of node
                //assume we explore children of a node in increasing order of the choices
                game.executeNextEvent(choices.get(curr.size()));
                final MCTSGameTree child = new MCTSGameTree(curr.size(), game.getScore());
                curr.addChild(child);
                path.add(child);
                return path;
            } else {
                while (curr.size() > choices.size()) {
                    System.err.println("ERROR! MCTS: invalid nodes! " + 
                            "curr has " + curr.size() + " children but there are only " + choices.size() + " choices");
                    System.err.println(event.toString());
                    curr.removeLast();
                }

                //curr.size() == choices.size()
                if (curr.size() == 0) {
                    System.err.println("ERROR! MCTS: curr has size 0, choices has size " + choices.size());
                    System.exit(1);
                }

                MCTSGameTree next = null;
                double bestV = Double.NEGATIVE_INFINITY;
                for (MCTSGameTree child : curr) {
                    //skip solved nodes
                    if (child.isSolved()) {
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
            if (game.getLosingPlayer() == null) {
                System.err.println("ERROR! game finished but no losing player");
                System.exit(1);
            } else if (game.getLosingPlayer() == game.getScorePlayer()) {
                node.setAILose();
                return -1.0;
            } else {
                node.setAIWin();
                return 1.0;
            }
        }

        final int startEvents = game.getEventsExecuted();
        getNextMultiChoiceEvent(game, true, true);
        final int events = game.getEventsExecuted() - startEvents;
        
        if (LOGGING) {
            LENS.add(events);
        }

        if (game.getLosingPlayer() == null) {
            return 0;
        } else if (game.getLosingPlayer() == game.getScorePlayer()) {
            return  -(1.0 - events/((double)MAXEVENTS));
        } else {
            return  1.0 - events/((double)MAXEVENTS);
        }
    }
    
    private List<Object[]> getNextMultiChoiceEvent(MagicGame game, boolean fastChoices, boolean sim) {
        game.setFastChoices(fastChoices);
        final int startEvents = game.getEventsExecuted();
        
        // simulate game until it is finished or simulated 300 events
        while (!game.isFinished() && (game.getEventsExecuted() - startEvents) < MAXEVENTS) {
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
                if (choice == null) {
                    System.err.println("ERROR! MCTS: no choice found during sim");
                    game.executeNextEvent(null);
                } else {
                    game.executeNextEvent(choice);
                }
            } else {
                //get list of possible AI choices
                final List<Object[]> choices = event.getArtificialChoiceResults(game);
                final int size = choices.size();
                if (size == 0) {
                    //invalid game state
                    System.err.println("ERROR! MCTS: no choice found");
                    game.executeNextEvent(null);
                } else if (size == 1) {
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
    private boolean isAI;
    private int maxChildren;
    private int numLose = 0;
    private int numSim = 0;
    private double score = 0;
    private int evalScore = 0;

    public MCTSGameTree(final int choice, final int evalScore) { 
        this.evalScore = evalScore;
        this.choice = choice;
    }

    public void setMaxChildren(final int mc) {
        maxChildren = mc;
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
            return -MCTSAI.BOOST + getNumSim();
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

/*
class CacheNode extends LinkedHashMap<Long, MCTSGameTree> {
	private static final long serialVersionUID = 1L;
    private final int capacity;
    public CacheNode(int capacity) {
        super(capacity + 1, 1.1f, true);
        this.capacity = capacity;
    }

    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > capacity;
    }
}
*/
