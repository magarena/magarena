package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestOverload extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Grizzly Bears",false,2);
        addToHand(P, "Blustersquall", 1);
        addToHand(P, "Chemister's Trick", 1);
        addToHand(P, "Counterflux", 1);
        addToHand(P, "Cyclonic Rift", 1);
        addToHand(P, "Electrickery", 1);
        addToHand(P, "Mizzium Mortars", 1);
        addToHand(P, "Street Spasm", 1);
        addToHand(P, "Vandalblast", 1);

        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Grizzly Bears",false,2);
        addToHand(P, "Downsize", 1);
        addToHand(P, "Dynacharge", 1);
        addToHand(P, "Mizzium Skin", 1);

        return game;
    }
}
