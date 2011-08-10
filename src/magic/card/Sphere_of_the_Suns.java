package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicTapAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicVividManaActivation;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Sphere_of_the_Suns {
    public static final MagicTrigger SPHERE_SUN=new MagicTrigger(MagicTriggerType.WhenComesIntoPlay) {
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
    
    public static final MagicManaActivation MANA=new MagicVividManaActivation(MagicManaType.ALL_TYPES);
}
