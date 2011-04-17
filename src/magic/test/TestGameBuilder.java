package magic.test;

import java.util.concurrent.atomic.AtomicInteger;

import magic.data.CardDefinitions;
import magic.data.TokenCardDefinitions;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicPlayerProfile;
import magic.model.MagicTournament;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicPutIntoPlayAction;
import magic.model.phase.MagicMainPhase;

public class TestGameBuilder {
	
	private static final AtomicInteger currentId=new AtomicInteger(1);
	
	public static void addToLibrary(final MagicPlayer player,final String name,final int count) {

		final MagicCardDefinition cardDefinition=CardDefinitions.getInstance().getCard(name);
		for (int c=count;c>0;c--) {
			
			player.getLibrary().addToTop(new MagicCard(cardDefinition,player,currentId.getAndIncrement()));
		}
	}
	
	public static void addToGraveyard(final MagicPlayer player,final String name,final int count) {
		
		final MagicCardDefinition cardDefinition=CardDefinitions.getInstance().getCard(name);
		for (int c=count;c>0;c--) {
			
			player.getGraveyard().addToTop(new MagicCard(cardDefinition,player,currentId.incrementAndGet()));
		}
	}

	public static void addToExile(final MagicPlayer player,final String name,final int count) {
		
		final MagicCardDefinition cardDefinition=CardDefinitions.getInstance().getCard(name);
		for (int c=count;c>0;c--) {
			
			player.getExile().addToTop(new MagicCard(cardDefinition,player,currentId.incrementAndGet()));
		}
	}
	
	public static void addToHand(final MagicPlayer player,final String name,final int count) {
		
		final MagicCardDefinition cardDefinition=CardDefinitions.getInstance().getCard(name);
		if (cardDefinition!=null) {
			for (int c=count;c>0;c--) {
				
				player.addCardToHand(new MagicCard(cardDefinition,player,currentId.incrementAndGet()));
			}
		}
	}

	public static void createAllTokens(final MagicGame game,final MagicPlayer player) {
		
		for (final MagicCardDefinition cardDefinition : TokenCardDefinitions.TOKEN_CARDS) {
			
			game.doAction(new MagicPlayTokenAction(player,cardDefinition));
		}
	}
	
	public static MagicPermanent createPermanent(final MagicGame game,final MagicPlayer player,final String name,final boolean tapped,final int count) {

		final MagicCardDefinition cardDefinition=CardDefinitions.getInstance().getCard(name);
		MagicPermanent lastPermanent=null;
		for (int c=count;c>0;c--) {
			
			final MagicCard card=new MagicCard(cardDefinition,player,currentId.getAndIncrement());
			final MagicPermanent permanent=new MagicPermanent(currentId.getAndIncrement(),card,player);
			lastPermanent=permanent;
			
			game.doAction(new MagicPutIntoPlayAction() {
				
				@Override
				protected MagicPermanent createPermanent(final MagicGame game) {

					return permanent;
				}
			});

			game.getEvents().clear();
			game.getStack().removeAllItems();
			permanent.clearState(MagicPermanentState.Summoned);
			if (tapped) {
				permanent.setState(MagicPermanentState.Tapped);
			} 
		}
		return lastPermanent;
	}
	
	public static MagicGame buildGame() {

		final MagicTournament tournament=new MagicTournament();
		tournament.setDifficulty(6);
		
		final MagicPlayerProfile profile=new MagicPlayerProfile("bgruw");
		final MagicPlayerDefinition player1=new MagicPlayerDefinition("Player",false,profile,15);
		final MagicPlayerDefinition player2=new MagicPlayerDefinition("Computer",true,profile,14);
		tournament.setPlayers(new MagicPlayerDefinition[]{player1,player2});
		tournament.setStartPlayer(0);
		
		final MagicGame game=tournament.nextGame();
		game.setPhase(MagicMainPhase.getFirstInstance());
		final MagicPlayer player=game.getPlayer(0);
		final MagicPlayer opponent=game.getPlayer(1);
		opponent.setLife(12);
		
		addToLibrary(player,"Plains",10);
		addToLibrary(opponent,"Island",10);
		addToGraveyard(player,"Mogg Fanatic",2);
		addToGraveyard(opponent,"Island",2);
		addToHand(opponent,"Zephyr Sprite",3);
		addToHand(player,"Shivan Dragon",1);
		addToHand(player,"Phyrexian Rager",1);
		addToHand(player,"Plague Myr",1);
		addToHand(player,"Tangle Mantis",1);
		addToHand(player,"Lumengrid Gargoyle",1);
		addToHand(player,"Scourge Servant",1);
		addToHand(player,"Tine Shrike",1);
		addToHand(player,"Oversoul of Dusk",1);
		addToHand(player,"Kuldotha Ringleader",1);
						
		createPermanent(game,player,"Raging Goblin",false,2);
		createPermanent(game,player,"Bonesplitter",false,1);
		createPermanent(game,player,"Rupture Spire",false,7);
		createPermanent(game,opponent,"Bloodrock Cyclops",false,1);
		createPermanent(game,opponent,"Silver Knight",false,1);
		
		return game;
	}
}