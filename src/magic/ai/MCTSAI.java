package magic.ai;

import java.util.Random;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import magic.model.MagicGame;
import magic.model.phase.MagicPhase;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;

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
@SuppressWarnings("unused")
public class MCTSAI implements MagicAI {
    
    private static final int MAXSIM = 500;
    private static final boolean LOGGING = true;
    private static final Random RNG = new Random();

    private static void log(final String message) {
        if (LOGGING) {
            System.out.println(message);
        }
    }
    
    private static void logc(final char message) {
        if (LOGGING) {
            System.out.print(message);
        }
    }
    
    public synchronized Object[] findNextEventChoiceResults(
            final MagicGame game, 
            final MagicPlayer scorePlayer) {

        final String pinfo = "MCTS " + scorePlayer.getIndex() + " (" + scorePlayer.getLife() + ")";
        
        final long startTime = System.currentTimeMillis();
        
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
        final MCTSGameTree root = new MCTSGameTree(-1);
        for (int i = 1; i <= MAXSIM; i++) {
            //create a new MagicGame for simulation
            final MagicGame start = new MagicGame(game, scorePlayer);
            
            //pass in a clone of the state, genNewTreeNode grows the tree by one node
            //and returns the path from the root to the new node
            final List<MCTSGameTree> path = genNewTreeNode(root, start);
            
            // play a simulated game to get score
            // update all nodes along the path from root to new node 
            final int score = randomPlay(start);
            logc((score == 1) ? '.' : 'X');
            for (MCTSGameTree node : path) {
                node.updateScore(score);
            }
        }
        logc('\n');

        //select the best choice (child that has the most number of simulations)
        double maxV = -1e10;
        int idx = -1;
        final List<ArtificialChoiceResults> achoices = getACR(choices);
        for (MCTSGameTree node : root.children()) {
            achoices.get(node.getChoice()).worker = node.getScore();
            achoices.get(node.getChoice()).gameCount = node.getNumSim();
            if (node.getV() >= maxV) {
                maxV = node.getV();
                idx = node.getChoice();
            }
        }

        final long duration = System.currentTimeMillis() - startTime;
        log("MCTS took " + duration);
        
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
    // where v[n] = 1 - n.value/n.nb + sqrt(2 * log(nb) / n.nb)
    // find a path from root to an unexplored node
    private List<MCTSGameTree> genNewTreeNode(MCTSGameTree root, MagicGame game) {
        List<MCTSGameTree> path = new LinkedList<MCTSGameTree>();
        MCTSGameTree curr = root;
        path.add(curr);

        for (MagicEvent event = getNextMultiChoiceEvent(game, curr != root);
             event != null;
             event = getNextMultiChoiceEvent(game, curr != root)) {

            final List<Object[]> choices = event.getArtificialChoiceResults(game);
            
            //multiple choices
            if (curr.size() < choices.size()) {
                //there are unexplored children of node
                //assume we explore children of a node in increasing order of the choices
                game.executeNextEvent(choices.get(curr.size()));
                MCTSGameTree child = new MCTSGameTree(curr.size());
                curr.addChild(child);
                path.add(child);
                return path;
            } else {
                final int totalSim = curr.getNumSim();
                double bestV = -1e10;
                for (MCTSGameTree node : curr.children) {
                    double v = 
                        ((game.getScorePlayer() == event.getPlayer()) ? 1.0 : -1.0) * node.getV() + 
                        Math.sqrt(2.0 * Math.log(totalSim) / node.getNumSim());
                    if (v >= bestV) {
                        bestV = v;
                        curr = node;
                    }
                }
                game.executeNextEvent(choices.get(curr.getChoice()));
                path.add(curr);
            }
        } 
        
        return path;
    }
        

    private int randomPlay(final MagicGame game) {
        // play game until it is finished
        for (MagicEvent event = getNextMultiChoiceEvent(game, true);
             event != null;
             event = getNextMultiChoiceEvent(game, true)) {
            final List<Object[]> choices = event.getArtificialChoiceResults(game);
            final int idx = RNG.nextInt(choices.size());
            final Object[] selected = choices.get(idx);
            //logc('-');
            game.executeNextEvent(selected);
        }

        // game is finished, check who lost
        if (game.getLosingPlayer() == game.getScorePlayer()) {
            return -1;
        } else {
            return 1;
        }
    }
    
    private MagicEvent getNextMultiChoiceEvent(MagicGame game, boolean fastChoices) {
        game.setFastChoices(fastChoices);
        while (!game.isFinished()) {
            if (game.hasNextEvent()) {
                final MagicEvent event=game.getNextEvent();
                if (event.hasChoice()) {
                    final List<Object[]> choices = event.getArtificialChoiceResults(game);
                    final int size = choices.size();
                    if (size == 0) {
                        //QQQ: when does this occur?
                        log("NO CHOICE");
                        return event;
                    } else if (size == 1) {
                        game.executeNextEvent(choices.get(0));
                    } else {
                        //multiple choice
                        return event;
                    }
                } else {
                    game.executeNextEvent(MagicEvent.NO_CHOICE_RESULTS);
                }
            } else {
                game.getPhase().executePhase(game);
            }
        }
        //game is finished
        return null;
    }
}

//only store one copy of MagicGame
//each tree node stores the choice from the parent that leads to this node
//so we only need one copy of MagicGame for MCTSAI
class MCTSGameTree {
    private final int choice;
    public List<MCTSGameTree> children = new LinkedList<MCTSGameTree>();
    private int numSim = 0;
    private int score = 0;

    public MCTSGameTree(int choice) {
        this.choice = choice;
    }

    public List<MCTSGameTree> children() {
        return children;
    }

    public int getChoice() {
        return choice;
    }

    public int getScore() {
        return score;
    }

    public void updateScore(final int score) {
        this.score += score;
        numSim += 1;
    }

    public int getNumSim() {
        return numSim;
    }

    public double getV() {
        return (double)score / numSim;
    }

    public void addChild(MCTSGameTree child) {
        children.add(child);
    }

    public int size() {
        return children.size();
    }
}
