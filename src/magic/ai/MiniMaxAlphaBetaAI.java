package magic.ai;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

public class MiniMaxAlphaBetaAI implements MagicAI {
	
    public Object[] findNextEventChoiceResults(final MagicGame game,final MagicPlayer player) {

    	final ArtificialWorkerPool aiWorkerPool = new ArtificialWorkerPool(game, player);
        return aiWorkerPool.findNextEventChoiceResults();
    }
}