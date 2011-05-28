package magic.test;

import magic.model.*;
import magic.model.phase.MagicMainPhase;

class TestGGvRR extends TestGameBuilder {    
    /**
     * Raging Ravine changed into 3/3 RG creature cannot block Guardian of the
     * Guildpack which has protection from monocolored
     * Fixed by making the protection check use getColorFlags in addition to getColoredTypeg
     */
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
	
        player.setLife(1);
		addToLibrary(player,"Plains",10);
		createPermanent(game,player,"Rupture Spire",false,8);
		createPermanent(game,player,"Raging Ravine",false,1);
		createPermanent(game,player,"Giant Spider",false,1);
		//createPermanent(game,player,"Esper Stormblade",false,1);
		createPermanent(game,player,"Naya Hushblade",false,1);
		//createPermanent(game,player,"Godsire",false,1);
		//createPermanent(game,player,"Murderous Redcap",false,1);
		
        opponent.setLife(1);
        addToLibrary(opponent,"Island",10);
        createPermanent(game,opponent,"Guardian of the Guildpact",false,5);
		
		return game;
    }
}
