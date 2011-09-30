package magic.test;

import magic.ai.MCTSAI;
import magic.ai.MagicAIImpl;
import magic.ai.MagicAI;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicPlayerProfile;
import magic.model.MagicTournament;
import magic.model.phase.MagicMainPhase;

class TestMCTS extends TestGameBuilder {    
    public MagicGame getGame() {
		final MagicTournament tournament=new MagicTournament();
		tournament.setDifficulty(6);
		
		final MagicPlayerProfile profile=new MagicPlayerProfile("bgruw");
		final MagicPlayerDefinition player1=new MagicPlayerDefinition("Player",false,profile,15);
		final MagicPlayerDefinition player2=new MagicPlayerDefinition("Computer",true,profile,14);
		tournament.setPlayers(new MagicPlayerDefinition[]{player1,player2});
		tournament.setStartPlayer(0);
        tournament.setAIs(new MagicAI[]{null, MagicAIImpl.MCTS.getAI()});
		
		final MagicGame game=tournament.nextGame(true);
		game.setPhase(MagicMainPhase.getFirstInstance());
		final MagicPlayer player=game.getPlayer(0);
		final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(4);
        addToLibrary(P, "Plains", 10);
		createPermanent(game,P,"Rupture Spire",false,8);
		createPermanent(game,P,"Hearthfire Hobgoblin", false, 4);
		createPermanent(game,P,"Akrasan Squire", false, 4);
       

        P = opponent;
		
        P.setLife(4);
        addToLibrary(P, "Plains", 10);
		createPermanent(game,P,"Rupture Spire",false,8);
		createPermanent(game,P,"Hearthfire Hobgoblin", false, 4);
		createPermanent(game,P,"Akrasan Squire", false, 4);
		
		return game;
    }
}
