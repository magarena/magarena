package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.*;

class TestSurge extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTS, 6);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicUpkeepPhase.getInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(10);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Forest", false, 8);
        createPermanent(P, "Chromatic Lantern", false, 1);
        addToHand(P, "Lightning Bolt", 2);
        addToHand(P, "Comparative Analysis", 1);
        addToHand(P, "Reckless Bushwhacker", 1);
        addToHand(P, "Tyrant of Valakut", 1);
        addToHand(P, "Crush of Tentacles", 1);

        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Forest", false, 8);
        createPermanent(P, "Chromatic Lantern", false, 1);
        createPermanent(P, "Wind Drake", false, 1);

        return game;
    }
}
