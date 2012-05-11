package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtEndOfTurnTrigger;

public class Archwing_Dragon {
    public static final MagicAtEndOfTurnTrigger T = new MagicAtEndOfTurnTrigger() {
		@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPlayer data) {
			final MagicPlayer player = permanent.getController();
			return (player == data) ?
				new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent},
                    this,
                    "Return " + permanent + " to its owner's hand."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicRemoveFromPlayAction(
					(MagicPermanent)data[0],
					MagicLocationType.OwnersHand));
		}
    };
}
