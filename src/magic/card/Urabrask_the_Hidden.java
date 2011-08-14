package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicTapEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Urabrask_the_Hidden {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenOtherComesIntoPlay) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPermanent otherPermanent=(MagicPermanent)data;
			return (otherPermanent.isCreature()&&otherPermanent.getController()!=permanent.getController()) ?
                new MagicTapEvent(otherPermanent):
                null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {}

		@Override
		public boolean usesStack() {
			return false;
		}		
    };
}
