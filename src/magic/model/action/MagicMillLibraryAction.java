package magic.model.action;

import java.util.ArrayList;
import java.util.List;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;

public class MagicMillLibraryAction extends MagicAction {

	private final MagicPlayer player;
	private int amount;
	private List<MagicCard> milledCards;
	
	public MagicMillLibraryAction(final MagicPlayer player,final int amount) {
		
		this.player=player;
		this.amount=amount;
	}

	@Override
	public void doAction(final MagicGame game) {

		milledCards=new ArrayList<MagicCard>();
		final MagicCardList library=player.getLibrary();
		final int size=library.size();
		final int count=size>=amount?amount:size;		
		if (count>0) {
			final MagicCardList graveyard=player.getGraveyard();
			setScore(player,ArtificialScoringSystem.getMillScore(count));
			for (int c=count;c>0;c--) {

				final MagicCard milledCard=library.removeCardAtTop();
				graveyard.addToTop(milledCard);
				milledCards.add(milledCard);
			}
			game.logMessage(player,"You put the top "+count+" cards of your library into your graveyard.");
		}
	}

	@Override
	public void undoAction(final MagicGame game) {

		final MagicCardList library=player.getLibrary();
		final MagicCardList graveyard=player.getGraveyard();
		for (int index=milledCards.size()-1;index>=0;index--) {
			
			final MagicCard milledCard=milledCards.get(index);
			graveyard.remove(milledCard);
			library.addToTop(milledCard);
		}
	}
}