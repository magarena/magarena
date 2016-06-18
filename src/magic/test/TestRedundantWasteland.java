package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicDeckProfile;
import magic.model.phase.*;
import magic.ai.MagicAIImpl;

/*
 * AI should attack with Bears and kill player in two turns.
 * Instead AI wastes the Wastelands to destroy Dark Citadel.
 */
class TestRedundantWasteland extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS, 6);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicUpkeepPhase.getInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(8);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Darksteel Citadel", false, 8);
        addToHand(P, "Lightning Bolt", 1);

        P = opponent;

        P.setLife(8);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Wasteland", false, 2);
        createPermanent(P, "Grizzly Bears", false, 2);

        return game;
    }
}
