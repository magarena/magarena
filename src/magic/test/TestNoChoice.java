
package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicDeckProfile;
import magic.model.phase.*;
import magic.ai.MagicAIImpl;

class TestNoChoice extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS, 1);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicUpkeepPhase.getInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(1);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Vesper Ghoul", false, 1);
        createPermanent(P, "Foul Familiar", false, 1);

        P = opponent;

        P.setLife(1);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Vesper Ghoul", false, 1);
        createPermanent(P, "Foul Familiar", false, 1);

        return game;
    }
}
