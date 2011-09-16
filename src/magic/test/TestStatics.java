
package magic.test;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicPlayerProfile;
import magic.model.MagicTournament;
import magic.model.phase.MagicMainPhase;

class TestStatics extends TestGameBuilder {    
    public MagicGame getGame() {
		final MagicTournament tournament=new MagicTournament();
		tournament.setDifficulty(6);
		
		final MagicPlayerProfile profile=new MagicPlayerProfile("bgruw");
		final MagicPlayerDefinition player1=new MagicPlayerDefinition("Player",false,profile,15);
		final MagicPlayerDefinition player2=new MagicPlayerDefinition("Computer",true,profile,14);
		tournament.setPlayers(new MagicPlayerDefinition[]{player1,player2});
		tournament.setStartPlayer(0);
		
		final MagicGame game=tournament.nextGame(true);
		game.setPhase(MagicMainPhase.getFirstInstance());
		final MagicPlayer player=game.getPlayer(0);
		final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
		createPermanent(game,P,"Rupture Spire",false,8);
        createPermanent(game,P,"Phyrexian Crusader",false,3);
	    addToHand(P,"Glorious Anthem",1); 
	    addToHand(P,"Godhead of Awe",1); 
	    addToHand(P,"Tolsimir Wolfblood",1); 
	    addToHand(P,"Aven Mimeomancer",1); 
	    addToHand(P,"Student of Warfare",1); 
       

        P = opponent;
		
        P.setLife(20);
        addToLibrary(P, "Plains", 10);
		createPermanent(game,P,"Rupture Spire",false,8);
        createPermanent(game,P,"Phyrexian Crusader",false,3);
	    addToHand(P,"Naturalize",2); 
		
		return game;
    }
}
