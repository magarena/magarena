package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestRustingGolem extends TestGameBuilder {
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
        addToHand(P,"Rusting Golem",1);
        addToHand(P,"Godhead of Awe",1);
        addToHand(P, "Mortivore", 1);
        addToGraveyard(P,"Mortivore", 10);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);

        return game;
    }
}
