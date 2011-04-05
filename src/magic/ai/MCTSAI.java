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
    
	private static final int MAXSIM = 100;
    private static final Random RNG = new Random();
    private static final boolean LOGGING=true;
    private MagicPlayer P = null;

    private static void log(final String message) {
        if (LOGGING) {
            System.out.println(message);
        }
    }
    
    public synchronized Object[] findNextEventChoiceResults(
            final MagicGame game, 
            final MagicPlayer scorePlayer) {

        P = scorePlayer;
        final long startTime = System.currentTimeMillis();
        final String pinfo = "MCTS " + P.getIndex() + " (" + P.getLife() + ")";
        final List<Object[]> choices = getCR(game, P);
        final int size = choices.size();
        
        // No choice results
        if (size == 0) {
            log(pinfo + " NO CHOICE");
            return null;
        }
    
        // Single choice result
        if (size == 1) {
            final Object selected[] = choices.get(0);
            log(pinfo + " " + Arrays.toString(selected));
            return game.map(selected);
        }
        
        // repeat a number of simulations
        // each simulation does the following
        //   selects a path down the game tree and create a new leaf
        //   score the leaf by doing a random play to the end of the game
        //   update the score of all the ancestors of the leaf
        // return the choice that has the most number of simulations
 
        //root represents the start state
        final GameTree root = new GameTree(-1);
        for (int i = 1; i <= MAXSIM; i++) {
            final MagicGame curr = new MagicGame(game, P);
            //curr.setFastChoices(true);
            
            //pass in a clone of the state, findPath modifies the second object
            final List<GameTree> path = findPath(root, curr);
            
            //retrieve the new node
            final int last = path.size() - 1;
            final GameTree child = path.get(last);
            
            // play a simulated game to score the new node (exploration)
            // update the ancestors of the new node 
            final int score = randomPlay(curr);
            for (GameTree node : path) {
                node.updateScore(score);
            }
        }

        //select the best choice (child that has the most number of simulations)
        double maxV = -1;
        int idx = -1;
        for (GameTree node : root.children()) {
            if (node.getV() >= maxV) {
                maxV = node.getV();
                idx = node.getChoice();
            }
        }
       
        /*
        int idx = rng.nextInt(size);
        */

        final List<ArtificialChoiceResults> achoices = getACR(choices);
        final long duration = System.currentTimeMillis() - startTime;
        
        log("MCTS took " + duration);
        log(pinfo); 
        
        final ArtificialChoiceResults selected = achoices.get(idx);
        for (final ArtificialChoiceResults achoice : achoices) {
            log((achoice == selected ? "* ":"  ") + selected);
        }
        
        return game.map(selected.choiceResults);
    }

    private List<Object[]> getCR(MagicGame game, MagicPlayer player) {
        final MagicGame choiceGame = new MagicGame(game, player);
        final MagicEvent event = choiceGame.getNextEvent();
        return event.getArtificialChoiceResults(choiceGame);
    }

    private List<ArtificialChoiceResults> getACR(List<Object[]> choices) {
        final List<ArtificialChoiceResults> aiChoiceResultsList = new ArrayList<ArtificialChoiceResults>();
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
    private List<GameTree> findPath(GameTree root, MagicGame game) {
        List<GameTree> path = new LinkedList<GameTree>();
        path.add(root);
        boolean foundNew = false;
        GameTree node = root;

        while (!game.isFinished() && !foundNew) {
            if (game.hasNextEvent()) {
                final MagicEvent event=game.getNextEvent();
                if (event.hasChoice()) {
                    final List<Object[]> choiceResultsList=event.getArtificialChoiceResults(game);
                    final int nrOfChoices=choiceResultsList.size();
                    if (nrOfChoices==0) {
                        log("MCTS " + P.getIndex() + " NO CHOICE");
                        System.exit(1);
                    } else if (nrOfChoices==1) {
                        game.executeNextEvent(choiceResultsList.get(0));
                    } else {
                        //multiple choices
                        if (node.size() < nrOfChoices) {
                            //there are unexplored children of node
                            //assume we explore children of a node in increasing order of the choices
                            game.executeNextEvent(choiceResultsList.get(node.size()));
                            GameTree child = new GameTree(node.size());
                            node.addChild(child);
                            path.add(child);
                            foundNew = true;
                        } else {
                            final int totalSim = node.getNumSim();
                            GameTree next = null;
                            double bestV = -1e10;
                            for (GameTree n : node.children) {
                                double v = 
                                    ((event.getPlayer().getIndex() == P.getIndex()) ? 1.0 : -1.0) * n.getV() + 
                                    Math.sqrt(2 * Math.log(totalSim) / n.getNumSim());
                                if (v > bestV) {
                                    bestV = v;
                                    next = n;
                                }
                            }
                            game.executeNextEvent(choiceResultsList.get(next.getChoice()));
                            path.add(next);
                        }
                    }
                } else {
                    game.executeNextEvent(MagicEvent.NO_CHOICE_RESULTS);
                }
            } else {
                game.getPhase().executePhase(game);
            }
        }
        
        return path;
    }

    private int randomPlay(final MagicGame game) {
        Random rng = new Random();

        // play game until it is finished
        while (!game.isFinished()) {
            if (game.hasNextEvent()) {
                final MagicEvent event=game.getNextEvent();
                if (event.hasChoice()) {
                    final List<Object[]> choices = event.getArtificialChoiceResults(game);
                    final int nc = choices.size();
                    assert(nc > 0);
                    game.executeNextEvent(choices.get(rng.nextInt(nc)));
                } else {
                    game.executeNextEvent(MagicEvent.NO_CHOICE_RESULTS);
                }
            } else {
                final MagicPhase phase=game.getPhase();
                phase.executePhase(game);
            }
        }

        // game is finished, check who lost
        if (game.getLosingPlayer().getIndex() == P.getIndex()) {
            return -1;
        } else {
            return 1;
        }
    }
}

//only store one copy of MagicGame
//each tree node stores the choice from the parent that leads to this node
//so we only need one copy of MagicGame for MCTSAI
class GameTree {
    private final int choice;
    public List<GameTree> children = new LinkedList<GameTree>();
    private int numSim = 0;
    private int score = 0;

    public GameTree(int choice) {
        this.choice = choice;
    }

    public List<GameTree> children() {
        return children;
    }

    public int getChoice() {
        return choice;
    }

    public int getScore() {
        return score;
    }

    public void updateScore(int score) {
        this.score += score;
        numSim += 1;
    }

    public int getNumSim() {
        return numSim;
    }

    public double getV() {
        return (double)score / numSim;
    }

    public void addChild(GameTree child) {
        children.add(child);
    }

    public int size() {
        return children.size();
    }
}
