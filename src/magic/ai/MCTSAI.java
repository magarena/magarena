package magic.ai;

import java.util.Random;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;

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
    
    private static final int MAXSIM = 100000;
    private static final double C = 0.3;
    private final boolean LOGGING;

    public MCTSAI() {
        this(false);
    }

    public MCTSAI(boolean printLog) {
        LOGGING = printLog;
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
    
    public synchronized Object[] findNextEventChoiceResults(
            final MagicGame game, 
            final MagicPlayer scorePlayer) {

        final int MAXTIME = (10000 / 6) * game.getArtificialLevel();
        final long startTime = System.currentTimeMillis();
        final String pinfo = "MCTS " + scorePlayer.getIndex() + " (" + scorePlayer.getLife() + ")";
        final List<Object[]> choices = getCR(game, scorePlayer);
        final int size = choices.size();
        
        // No choice results
        if (size == 0) {
            log(pinfo + " NO CHOICE");
            return null;
        }
    
        // Single choice result
        if (size == 1) {
            final ArtificialChoiceResults selected = getACR(choices).get(0);
            log(pinfo + " " + selected);
            return game.map(selected.choiceResults);
        }
        
        // repeat a number of simulations
        // each simulation does the following
        //   selects a path down the game tree and create a new leaf
        //   score the leaf by doing a random play to the end of the game
        //   update the score of all the ancestors of the leaf
        // return the "best" choice
       
        //root represents the start state
        final MCTSGameTree root = new MCTSGameTree(-1, -1);
        int numSim = 0;
        for (int i = 1; i <= MAXSIM && System.currentTimeMillis() - startTime < MAXTIME; i++) {
            //create a new MagicGame for simulation
            final MagicGame start = new MagicGame(game, scorePlayer);
            //start.setKnownCards();
            
            //pass in a clone of the state, genNewTreeNode grows the tree by one node
            //and returns the path from the root to the new node
            final List<MCTSGameTree> path = genNewTreeNode(root, start);
            
            // play a simulated game to get score
            // update all nodes along the path from root to new node 
            final double score = randomPlay(start);
            logc((score > 0.0) ? '.' : 'X');
            for (MCTSGameTree node : path) {
                node.updateScore(score);
            }

            numSim++;
        }
        logc('\n');

        //select the best choice (child that has the highest secure score)
        double maxV = -1e10;
        int maxS = 0;
        int idx = -1;
        final List<ArtificialChoiceResults> achoices = getACR(choices);
        for (MCTSGameTree node : root) {
            achoices.get(node.getChoice()).worker = (int)(node.getV() * 100);
            achoices.get(node.getChoice()).gameCount = node.getNumSim();
            if (node.getNumSim() > maxV) { 
                maxV = node.getNumSim();
                maxS = node.getEvalScore();
                idx = node.getChoice();
            }
        }
        
        final long duration = System.currentTimeMillis() - startTime;
        log("MCTS took " + duration + "ms to run " + numSim + " simulations");
        
        log(pinfo); 
        final ArtificialChoiceResults selected = achoices.get(idx);
        for (final ArtificialChoiceResults achoice : achoices) {
            log((achoice == selected ? "* ":"  ") + achoice);
        }
        
        return game.map(selected.choiceResults);
    }

    private List<Object[]> getCR(final MagicGame game, final MagicPlayer player) {
        final MagicGame choiceGame = new MagicGame(game, player);
        final MagicEvent event = choiceGame.getNextEvent();
        return event.getArtificialChoiceResults(choiceGame);
    }

    private List<ArtificialChoiceResults> getACR(final List<Object[]> choices) {
        final List<ArtificialChoiceResults> aiChoiceResultsList = 
            new ArrayList<ArtificialChoiceResults>();
        for (final Object choiceResults[] : choices) {
            aiChoiceResultsList.add(new ArtificialChoiceResults(choiceResults));
        }
        return aiChoiceResultsList;
    }

    // p is parent of n
    // n.nb is how many times the node n is simulated
    // sum of nb in all children of parent of n (same as p.nb)
    // select node n (child of node) that maximize v[n]
    // where v[n] = 1 - n.value/n.nb + C * sqrt(log(nb) / n.nb)
    // find a path from root to an unexplored node
    private List<MCTSGameTree> genNewTreeNode(final MCTSGameTree root, final MagicGame game) {
        final List<MCTSGameTree> path = new LinkedList<MCTSGameTree>();
        MCTSGameTree curr = root;
        path.add(curr);

        for (List<Object[]> choices = getNextMultiChoiceEvent(game, curr != root);
             choices != null;
             choices = getNextMultiChoiceEvent(game, curr != root)) {
          
            final MagicEvent event = game.getNextEvent();
            assert choices.size() > 1 : "number of choices is " + choices.size();
            
            if (curr.size() < choices.size()) {
                //there are unexplored children of node
                //assume we explore children of a node in increasing order of the choices
                game.executeNextEvent(choices.get(curr.size()));
                final MCTSGameTree child = new MCTSGameTree(curr.size(), game.getScore());
                curr.addChild(child);
                path.add(child);
                return path;
            } else {
                final int totalSim = curr.getNumSim();
                double bestV = -1e10;
                MCTSGameTree child = curr.first();
                for (MCTSGameTree node : curr) {
                    if (node.getChoice() >= choices.size()) {
                        log("MCTS: INVALID NODE");
                        continue;
                    }

                    final double v = 
                        ((game.getScorePlayer() == event.getPlayer()) ? 1.0 : -1.0) * node.getV() + 
                        C * Math.sqrt(Math.log(totalSim) / node.getNumSim());
                    if (v > bestV) {
                        bestV = v;
                        child = node;
                    }
                }

                //move down the tree
                curr = child;
                assert curr != null;
                
                game.executeNextEvent(choices.get(curr.getChoice()));
                path.add(curr);
            }
        } 
       
        //game is finished
        assert game.isFinished() : "game is not finished";
        return path;
    }
        

    private double randomPlay(final MagicGame game) {
        // play game until it is finished
        for (List<Object[]> choices = getNextMultiChoiceEvent(game, true);
             choices != null;
             choices = getNextMultiChoiceEvent(game, true)) {
            final int idx = MagicRandom.nextInt(choices.size());
            final Object[] selected = choices.get(idx);
            //logc('-');
            game.executeNextEvent(selected);
        }
        
        // game is finished or in an invalid state
        assert (game.getMainPhaseCount() > 0) : "main phase count is zero";
	
        final int mainPhaseCount = 100000000;
        final int length = Math.max(1,mainPhaseCount - game.getMainPhaseCount());
      
        if (game.getLosingPlayer() == null) {
            return 0;
        } else if (game.getLosingPlayer() == game.getScorePlayer()) {
            return -1.0/Math.pow(length, 0.25);
        } else {
            return 1.0/Math.pow(length, 0.25);
        }
    }
    
    private List<Object[]> getNextMultiChoiceEvent(MagicGame game, boolean fastChoices) {
        game.setFastChoices(fastChoices);
        
        while (!game.isFinished()) {
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
            final List<Object[]> choices = event.getArtificialChoiceResults(game);
            final int size = choices.size();
            if (size == 0) {
                //invalid game state
                return null;
            } else if (size == 1) {
                game.executeNextEvent(choices.get(0));
            } else {
                //multiple choice
                return choices;
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
    private final int choice;
    private final List<MCTSGameTree> children = new LinkedList<MCTSGameTree>();
    private int numSim = 0;
    private double score = 0;
    private int evalScore = 0;

    public MCTSGameTree(int choice, int evalScore) {
        this.evalScore = evalScore;
        this.choice = choice;
    }
    
    public void updateScore(final double score) {
        this.score += score;
        numSim += 1;
    }

    public int getChoice() {
        return choice;
    }

    public int getEvalScore() {
        return evalScore;
    }

    public double getScore() {
        return score;
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
