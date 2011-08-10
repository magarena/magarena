package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Juniper_Order_Ranger {

    public static final MagicTrigger V7650 =new MagicTrigger(MagicTriggerType.WhenOtherComesIntoPlay,"Juniper Order Ranger") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPermanent otherPermanent=(MagicPermanent)data;
			final MagicPlayer player=permanent.getController();
			if (otherPermanent!=permanent&&otherPermanent.isCreature()&&otherPermanent.getController()==player) {
				return new MagicEvent(permanent,player,new Object[]{otherPermanent,permanent},this,
					"Put a +1/+1 counter on "+otherPermanent.getName()+" and a +1/+1 counter on Juniper Order Ranger.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,1,true));
			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[1],MagicCounterType.PlusOne,1,true));			
		}		
    };
    
}
