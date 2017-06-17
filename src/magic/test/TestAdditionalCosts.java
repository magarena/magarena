package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestAdditionalCosts extends TestGameBuilder {
    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(1);
        addToLibrary(P, "Plains", 10);
        addToGraveyard(P,"Worthy Cause",2);
        createPermanent(P,"Island",false,4);
        createPermanent(P,"Plains",false,2);
        createPermanent(P,"Grizzly Bears",false,1);
        addToHand(P,"Snapcaster Mage",2);


        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Plains",false,8);
        addToHand(P,"Thieving Magpie",3);

        return game;
    }
}
