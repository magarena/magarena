package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicLayer;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Crescendo_of_War {
	
	private static int amount = 0;
	
	public static final MagicStatic S1 = new MagicStatic(
	        MagicLayer.ModPT, 
		    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
		@Override
		public void getPowerToughness(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
					pt.add(amount,0);
		}
		@Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
			if (source.hasCounters()) {
        		amount = source.getCounters(MagicCounterType.Charge);
        	}
            return target.isBlocking();
        }
    };
    
	public static final MagicStatic S2 = new MagicStatic(
	        MagicLayer.ModPT, 
		    MagicTargetFilter.TARGET_CREATURE) {
		@Override
		public void getPowerToughness(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
					pt.add(amount,0);
		}
		@Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
			if (source.hasCounters()) {
        		amount = source.getCounters(MagicCounterType.Charge);
        	}
            return target.isAttacking();
        }
    };
    
    public static final MagicAtUpkeepTrigger T2 = new MagicAtUpkeepTrigger() {
    	@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
			return new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{permanent},
                        this,
                        "Put a strife counter on " + permanent + ".");
		}	
    	@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
    		game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.Charge,1,true));
		}
	};
}
