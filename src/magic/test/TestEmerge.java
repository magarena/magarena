package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.*;

class TestEmerge extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTSC, 6);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicUpkeepPhase.getInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(10);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Darksteel Citadel", false, 2);
        createPermanent(P, "Chromatic Lantern", false, 1);
        createPermanent(P, "Wretched Gryff", false, 1);
        createPermanent(P, "Grizzly Bears", false, 1);
        addToHand(P, "Wretched Gryff", 1);

        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Forest", 20);

        return game;
    }
}
