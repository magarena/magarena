package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestEmbalm extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P, "Plains", 10);
        createPermanent(P,"Chromatic Lantern");
        createPermanent(P,"Honored Hydra");
        addToGraveyard(P, "Honored Hydra");
        addToHand(P, "Abrupt Decay");


        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Chromatic Lantern");
        createPermanent(P,"Sun Titan");

        return game;
    }
}
