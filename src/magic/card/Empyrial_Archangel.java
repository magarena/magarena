package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Empyrial_Archangel {

    public static final MagicTrigger V7232 =new MagicTrigger(MagicTriggerType.IfDamageWouldBeDealt,"Empyrial Archangel",2) {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getTarget()==permanent.getController()) {
				// Replacement effect. Generates no event or action.
				damage.setTarget(permanent);				
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
		}
    };
    
}
