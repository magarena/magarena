package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestMCTSCrash extends TestGameBuilder {
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
        createPermanent(P,"Sword of Feast and Famine",false,2);
        createPermanent(P,"Marisi's Twinclaws",false, 2);


        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        addToHand(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Mogg Fanatic", false, 1);
        createPermanent(P,"Knight of Meadowgrain", false, 2);

        return game;
    }
}
