

package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicDeckProfile;
import magic.model.phase.*;
import magic.ai.MagicAIImpl;

class TestCostIncrease extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS, 6);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicUpkeepPhase.getInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(4);
        addToLibrary(P, "Mountain", 20);
        createPermanent(P, "Mountain", false, 6);
        createPermanent(P, "Thorn of Amethyst", false, 1);
        addToHand(P, "Thalia, Guardian of Thraben", 1);
        addToHand(P, "Daru Warchief", 1);

        P = opponent;

        P.setLife(4);
        addToLibrary(P, "Mountain", 2);
        createPermanent(P, "Mountain", false, 6);

        return game;
    }
}
