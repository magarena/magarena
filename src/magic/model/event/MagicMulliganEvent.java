package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicMulliganChoice;

public class MagicMulliganEvent extends MagicEvent {

	private static final MagicEventAction EVENT_ACTION = new MagicEventAction() {
		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object[] data,
				final Object[] choiceResults) {
			final MagicPlayer player = (MagicPlayer)data[0];
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicCardList hand = new MagicCardList(player.getHand());
				final int size = player.getHand().size();
				for (final MagicCard card : hand) {
					game.doAction(new MagicMoveCardAction(
							card,
							MagicLocationType.OwnersHand,
							MagicLocationType.OwnersLibrary));
					game.doAction(new MagicRemoveCardAction(
							card,
							MagicLocationType.OwnersHand));
				}
				final MagicCardList library = player.getLibrary();
				library.shuffle(library.getCardsId());
				game.doAction(new MagicDrawAction(player,size - 1));
					game.addEvent(new MagicMulliganEvent(player));
			}
		}
	};
	
	public MagicMulliganEvent(final MagicPlayer player) {
        super(
        	MagicEvent.NO_SOURCE,
            player,
            new MagicMulliganChoice(),
            new Object[]{player},
            EVENT_ACTION,
            player + " may$ take a mulligan."
        );
	}
}
