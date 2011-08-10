package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Guard_Gomazoa {

    public static final MagicTrigger V7481 =new MagicTrigger(MagicTriggerType.IfDamageWouldBeDealt,"Guard Gomazoa",1) {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getTarget()==permanent&&damage.isCombat()) {
				// Replacement effect. Generates no event or action.
				damage.setAmount(0);	
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
		}
    };
    
}
