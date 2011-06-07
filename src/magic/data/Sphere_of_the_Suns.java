package magic.data;

import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.*;

class Sphere_of_the_Suns {
    static final MagicTrigger SPHERE_SUN=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
		    return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,
			    permanent.getName()+" enters the battlefield tapped with three charge counters on it.");
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {

            final MagicPermanent permanent=(MagicPermanent)data[0];
            game.doAction(new MagicTapAction(permanent,false));
            game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.Charge,3,false));
		}
	
        @Override
    	public boolean usesStack() {
    		return false;
	    }
    };
    
    static final MagicManaActivation MANA=new MagicVividManaActivation(MagicManaType.ALL_TYPES);
}
