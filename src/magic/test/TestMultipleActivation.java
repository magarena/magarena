package magic.test;

import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.phase.MagicMainPhase;

class TestMultipleActivation extends TestGameBuilder {

    public MagicGame getGame() {
        final MagicDuel duel=new MagicDuel();

        final MagicDeckProfile profile=new MagicDeckProfile("bgruw");
        final MagicPlayerDefinition player1=new MagicPlayerDefinition("Player",false,profile);
        final MagicPlayerDefinition player2=new MagicPlayerDefinition("Computer",true,profile);
        duel.setPlayers(new MagicPlayerDefinition[]{player1,player2});
        duel.setStartPlayer(0);

        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = opponent;

        P.setLife(2);
        addToLibrary(P,"Swamp",10);
        createPermanent(game,P,"Swamp",false,4);
        createPermanent(game,P,"Nantuko Shade",false,1);

        P = player;

        P.setLife(10);
        addToLibrary(P,"Swamp",10);
        createPermanent(game,P,"Swamp",true,10);
        addToHand(P,"Wall of Reverence",1);
        addToHand(P,"Skithiryx, the Blight Dragon",1);

        return game;
    }
}
