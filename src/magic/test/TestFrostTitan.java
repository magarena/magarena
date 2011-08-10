package magic.test;

import magic.model.*;
import magic.model.phase.MagicMainPhase;

class TestFrostTitan extends TestGameBuilder {    
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
	    addToHand(P,"Lightning Bolt",3); 
	    addToHand(P,"Frost Titan",3);
       

        P = opponent;
		
        P.setLife(20);
        addToLibrary(P, "Plains", 10);
		createPermanent(game,P,"Rupture Spire",false,8);
        createPermanent(game,P,"Phyrexian Crusader",false,2);
        createPermanent(game,P,"Phyrexian Crusader",true,1);
        createPermanent(game,P,"Frost Titan",true,1);
        createPermanent(game,P,"Frost Titan",false,1);
	    addToHand(P,"Lightning Bolt",3);
		
		return game;
    }
}
