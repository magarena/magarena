package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestDoubleFaced extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(5);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Island", false, 8);
        createPermanent(P, "Chromatic Lantern", false, 1);
        addToHand(P, "Thraben Sentry", 2);
        addToHand(P, "Lightning Bolt", 1);
        addToHand(P, "Ludevic's Test Subject", 2);

        P = opponent;

        P.setLife(1);
        addToLibrary(P, "Forest", 20);
        createPermanent(P, "Island", false, 8);
        createPermanent(P, "Grizzly Bears", false, 1);
        addToHand(P, "Ludevic's Test Subject", 2);

        return game;
    }
}
