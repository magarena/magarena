package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;

public class MagicMillLibraryAction extends MagicAction {

	private final MagicPlayer player;
	private final int amount;
	
	public MagicMillLibraryAction(final MagicPlayer player,final int amount) {
		
		this.player=player;
		this.amount=amount;
	}

	@Override
	public void doAction(final MagicGame game) {
		final MagicCardList library=player.getLibrary();
		final int size=library.size();
		final int count=size>=amount?amount:size;		
		if (count>0) {
			setScore(player,ArtificialScoringSystem.getMillScore(count));
			for (int c=count;c>0;c--) {
				final MagicCard milledCard = library.getCardAtTop();
				game.doAction(new MagicRemoveCardAction(milledCard,MagicLocationType.OwnersLibrary));
				game.doAction(new MagicMoveCardAction(milledCard,MagicLocationType.OwnersLibrary,MagicLocationType.Graveyard));
			}
			game.logMessage(player,"You put the top "+count+" cards of your library into your graveyard.");
		}
	}

	@Override
	public void undoAction(final MagicGame game) {}
}