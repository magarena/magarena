package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.mstatic.MagicLayer;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Kabira_Vindicator {
    public static final MagicStatic S1 = new MagicStatic(MagicLayer.SetPT) {
		@Override
		public void getPowerToughness(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
			final int charges = permanent.getCounters(MagicCounterType.Charge);
			if (charges >= 5) {
				pt.set(4,8);
			} else if (charges >= 2) {
				pt.set(3,6);
			}
		}
    };

    public static final MagicStatic S2 = new MagicStatic(
    		MagicLayer.ModPT, 
    		MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {

    	private int amount = 0;

    	@Override
    	public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
    		pt.add(amount, amount);
    	}
    	@Override
    	public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
    		if (source.hasCounters()) {
    			final int charges = source.getCounters(MagicCounterType.Charge);
    			if (charges >= 5) {
    				amount = 2;
    			} else if (charges >= 2) {
    				amount = 1;
    			} else {
    				amount = 0;
    			}
    		}
    		return source != target;
    	}
    };
}
