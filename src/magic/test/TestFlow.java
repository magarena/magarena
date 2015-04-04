package magic.test;

import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.phase.MagicMainPhase;

class TestFlow extends TestGameBuilder {
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

        P.setLife(4);
        P.setPoison(0);
        addToLibrary(P,"Plains",10);
        createPermanent(game,P,"Rupture Spire",false,8);
        //createPermanent(game,P,"Grizzly Bears",false,1);
        createPermanent(game,P,"Chameleon Colossus",false,1);


        P = opponent;

        P.setLife(2);
        P.setPoison(0);
        addToLibrary(P,"Island",10);
        createPermanent(game,P,"Rupture Spire",false,5);
        createPermanent(game,P,"Tectonic Edge",false,3);
        createPermanent(game,P,"Grizzly Bears",false,1);

        return game;
    }
}
