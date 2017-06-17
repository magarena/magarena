package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestFrostTitan extends TestGameBuilder {
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
        addToHand(P,"Lightning Bolt",3);
        addToHand(P,"Vines of Vastwood",1);
        addToHand(P,"Frost Titan",3);


        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(P,"Rupture Spire",false,8);
        createPermanent(P,"Phyrexian Crusader",false,2);
        createPermanent(P,"Phyrexian Crusader",true,1);
        createPermanent(P,"Frost Titan",true,1);
        createPermanent(P,"Frost Titan",false,1);
        //addToHand(P,"Lightning Bolt",3);

        return game;
    }
}
