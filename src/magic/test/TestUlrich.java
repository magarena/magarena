package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.*;

class TestUlrich extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTSC, 6);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicUpkeepPhase.getInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(10);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Wastes", false, 8);
        createPermanent(P, "Chromatic Lantern", false, 1);
        createPermanent(P, "Vildin-Pack Alpha", false, 1);
        addToHand(P, "Ulrich of the Krallenhorde", 1);
        addToHand(P, "Lightning Bolt", 2);

        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Colossodon Yearling", false, 2);
        createPermanent(P, "Grizzly Bears", false, 1);

        return game;
    }
}
