package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestNantukoShade extends TestGameBuilder {

    @Override
    public MagicGame getGame() {
        final MagicDuel duel=createDuel();
        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = opponent;

        P.setLife(2);
        addToLibrary(P,"Swamp",10);
        createPermanent(P,"Swamp",false,4);
        createPermanent(P,"Nantuko Shade",false,1);

        P = player;

        P.setLife(5);
        addToLibrary(P,"Plains",10);
        createPermanent(P,"Plains",true,4);
        //createPermanent(P,"Siege Mastodon",false,1);
        addToHand(P,"Wall of Reverence",1);

        return game;
    }
}
