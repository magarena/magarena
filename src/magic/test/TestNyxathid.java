package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestNyxathid extends TestGameBuilder {

    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Mountain", 10);
        createPermanent(P,"Rupture Spire",false,6);
        createPermanent(P,"Nyxathid",false,1);
        addToHand(P,"Eager Cadet",1);


        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Mountain", 10);
        createPermanent(P,"Rupture Spire",false,6);
        createPermanent(P,"Eager Cadet",false,1);
        addToHand(P,"Sift",1);
        addToHand(P,"Mountain",3);

        return game;
    }
}
