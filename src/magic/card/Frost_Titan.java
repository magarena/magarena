package magic.card;

import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;

public class Frost_Titan {
    public static final MagicTrigger T1 = new MagicTrigger(MagicTriggerType.WhenTargeted) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
            final MagicSource source = (MagicSource)data;
            if (source.getController() != permanent.getController()) {
                //counter source unless its controller pay {2}
            }
            return null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
		
        }
    };
   
    //tap target permanent. It doesn't untap during its controller's next untap step.
    public static final MagicTrigger T2 = new MagicTrigger(MagicTriggerType.WhenComesIntoPlay) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
		    return null;
        }
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
		}
    };
    
    public static final MagicTrigger T3 = new MagicTrigger(MagicTriggerType.WhenAttacks) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
		    return null;
        }
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
		}
    };
}
