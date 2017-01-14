package magic.test;

import magic.ai.MagicAIImpl;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicUpkeepPhase;

class TestMiracle extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel(MagicAIImpl.MCTSC, 6);
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicUpkeepPhase.getInstance());
        game.setTurn(2);
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(10);
        addToLibrary(P, "Forest", 20);
        addToLibrary(P, "Terminus");
        createPermanent(P, "Wastes", 8);
        createPermanent(P, "Chromatic Lantern");
        createPermanent(P, "Grizzly Bears",4);

        P = opponent;

        P.setLife(15);
        addToLibrary(P, "Forest", 20);
        addToLibrary(P, "Terminus");
        createPermanent(P, "Wastes", 8);
        createPermanent(P, "Chromatic Lantern");
        createPermanent(P, "Grizzly Bears");

        return game;
    }
}
