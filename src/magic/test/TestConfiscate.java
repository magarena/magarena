package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestConfiscate extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(10);
        addToLibrary(P, "Plains", 20);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Grizzly Bears",false,1);
        addToHand(P,"Confiscate",1);


        P = opponent;

        P.setLife(10);
        addToLibrary(P, "Plains", 20);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Serra Angel",false,1);
        addToHand(P,"Sword of Body and Mind",1);

        return game;
    }
}
