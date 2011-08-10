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
