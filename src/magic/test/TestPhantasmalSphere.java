package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestPhantasmalSphere extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(13);
        addToLibrary(P, "Island", 20);
        createPermanent(P, "Island", 5);
        createPermanent(P, "Phantasmal Sphere");

        P = opponent;

        P.setLife(13);
        addToLibrary(P, "Island", 20);
        createPermanent(P, "Island", 5);
        addToHand(P, "Phantasmal Sphere", 1);

        return game;
    }
}
