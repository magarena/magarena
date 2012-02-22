package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;

public class Black_Cat {
	public static final MagicWhenPutIntoGraveyardTrigger T = new MagicWhenPutIntoGraveyardTrigger() {
		@Override
		public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicGraveyardTriggerData triggerData) {
			final MagicPlayer opponent = game.getOpponent(permanent.getController());
			return (MagicLocationType.Play == triggerData.fromLocation) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{permanent,opponent},
                        this,
                        opponent + " discards a card at random."):
                MagicEvent.NONE;
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.addEvent(new MagicDiscardEvent(
					(MagicPermanent)data[0],
					(MagicPlayer)data[1],
					1,
					true));
		}
    };
}
