package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestExtort extends TestGameBuilder {
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
        addToHand(P, "Basilica Guards", 1);
        addToHand(P, "Basilica Screecher", 1);
        addToHand(P, "Blind Obedience", 1);
        addToHand(P, "Kingpin's Pet", 1);
        addToHand(P, "Knight of Obligation", 1);
        addToHand(P, "Syndic of Tithes", 1);
        addToHand(P, "Syndicate Enforcer", 1);

        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        addToHand(P, "Grizzly Bears", 1);
        addToHand(P, "Manalith", 1);

        return game;
    }
}
