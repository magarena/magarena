package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Knight_of_Cliffhaven {
	public static final MagicStatic S1 = new MagicStatic(MagicLayer.SetPT) {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			final int charges = permanent.getCounters(MagicCounterType.Charge);
			if (charges >= 4) {
				pt.set(4,4);
			} else if (charges >= 1) {
				pt.set(2,3);
			}
		}		
	};
	
	public static final MagicStatic S2 = new MagicStatic(MagicLayer.Ability) {
		@Override
		public long getAbilityFlags(
                final MagicGame game,
                final MagicPermanent permanent,
                final long flags) {
			final int charges = permanent.getCounters(MagicCounterType.Charge);
			if (charges >= 4) {
				return flags |
					MagicAbility.Flying.getMask() |
					MagicAbility.Vigilance.getMask();
			} else if (charges >= 1) {
				return flags | MagicAbility.Flying.getMask();
			} else {
                return flags;
            }
		}
    };
}
