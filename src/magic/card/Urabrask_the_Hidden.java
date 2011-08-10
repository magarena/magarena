package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicTapEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Urabrask_the_Hidden {

    public static final MagicTrigger V9273 =new MagicTrigger(MagicTriggerType.WhenOtherComesIntoPlay,"Urabrask the Hidden") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPermanent otherPermanent=(MagicPermanent)data;
			if (otherPermanent.isCreature()&&otherPermanent.getController()!=permanent.getController()) {
				return new MagicTapEvent(otherPermanent);
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

		}

		@Override
		public boolean usesStack() {

			return false;
		}		
    };
    
}
