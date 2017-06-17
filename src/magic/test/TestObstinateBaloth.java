package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestObstinateBaloth extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(6);
        addToLibrary(P, "Forest", 20);
        addToLibrary(P, "Island", 20);
        createPermanent(P,"Rupture Spire",false,9);
        createPermanent(P, "Grizzly Bears", false, 2);
        addToHand(P, "Mind Rot", 1);

        P = opponent;

        P.setLife(6);
        addToLibrary(P, "Mountain", 20);
        createPermanent(P,"Rupture Spire",false,9);
        createPermanent(P, "Blight Mamba", false, 1);
        addToHand(P, "Obstinate Baloth", 2);

        return game;
    }
}
