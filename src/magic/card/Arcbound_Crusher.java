package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Arcbound_Crusher {
    public static final MagicWhenOtherComesIntoPlayTrigger T3 = new MagicWhenOtherComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
			return (otherPermanent != permanent &&
					otherPermanent.isArtifact()) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{permanent},
                        this,
                        "Put a +1/+1 counter on " + permanent + "."):
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeCountersAction(
					(MagicPermanent)data[0],
					MagicCounterType.PlusOne,
					1,
					true));			
		}		
    };
}
