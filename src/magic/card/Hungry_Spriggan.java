package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Hungry_Spriggan {

    public static final MagicTrigger V7630 =new MagicTrigger(MagicTriggerType.WhenAttacks,"Hungry Spriggan") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			if (permanent==data) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,"Hungry Spriggan gets +3/+3 until end of turn.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],3,3));
		}
    };

}
