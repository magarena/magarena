package magic.test;

import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.phase.MagicMainPhase;

class TestMultipleActivation extends TestGameBuilder {

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

        P.setLife(10);
        addToLibrary(P,"Swamp",10);
        createPermanent(P,"Swamp",true,10);
        addToHand(P,"Wall of Reverence",1);
        addToHand(P,"Skithiryx, the Blight Dragon",1);

        return game;
    }
}
