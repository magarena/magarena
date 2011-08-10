package magic.card;
import java.util.*;
import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;
import magic.data.*;
import magic.model.variable.*;

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
