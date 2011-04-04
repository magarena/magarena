package magic.ai;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

public class MiniMaxAlphaBetaAI implements MagicAI {
    public Object[] findNextEventChoiceResults(MagicGame game, MagicPlayer player) {
        ArtificialWorkerPool ai = new ArtificialWorkerPool(game, player);
        return ai.findNextEventChoiceResults();
    }
}
