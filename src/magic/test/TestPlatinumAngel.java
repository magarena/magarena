
package magic.test;

import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.phase.MagicMainPhase;

class TestPlatinumAngel extends TestGameBuilder {
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

        MagicPlayer P = player;

        P.setLife(2);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Swamp",false,4);
        createPermanent(game,P,"Plains",false,2);
        createPermanent(game,P,"Platinum Angel",false,1);
        createPermanent(game,P,"Air Servant",false,1);
        addToHand(P,"Doom Blade",2);
        addToHand(P,"Demystify",2);
        addToHand(P,"Stomping Ground",1);


        P = opponent;

        P.setLife(2);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Swamp",false,4);
        createPermanent(game,P,"Plains",false,2);
        createPermanent(game,P,"Platinum Angel",false,1);
        createPermanent(game,P,"Alpha Myr",false,1);

        return game;
    }
}
