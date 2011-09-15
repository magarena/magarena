
package magic.test;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicPlayerProfile;
import magic.model.MagicTournament;
import magic.model.phase.MagicMainPhase;

class TestAbilities extends TestGameBuilder {    
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
		createPermanent(game,P,"Rupture Spire",false,20);
		createPermanent(game,P,"Accorder Paladin",false,3);	
		createPermanent(game,P,"Lightning Greaves",false,1);
		createPermanent(game,P,"Sword of Body and Mind",false,1);	
		createPermanent(game,P,"Darksteel Axe",false,1);
		createPermanent(game,P,"Akroma's Memorial",false,1);						
	    addToHand(P,"Naturalize",5); 
	    addToHand(P,"Demystify",3); 
	    addToHand(P,"Doom Blade",5); 
	    addToHand(P,"Boar Umbra",1); 
	    addToHand(P,"Brink of Disaster",1); 
	    addToHand(P,"Pacifism",1); 
       

        P = opponent;
		
        P.setLife(20);
        addToLibrary(P, "Plains", 10);
		createPermanent(game,P,"Rupture Spire",false,8);
		createPermanent(game,P,"Sword of Body and Mind",false,1);
		createPermanent(game,P,"Bladed Pinions",false,1);
        createPermanent(game,P,"Acidic Slime",false,3);
	    addToHand(P,"Unquestioned Authority",1); 
		
		return game;
    }
}
