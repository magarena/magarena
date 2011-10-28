package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.event.MagicLevelUpActivation;
import magic.model.event.MagicPermanentActivation;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Transcendent_Master {
	public static final MagicStatic S1 = new MagicStatic(MagicLayer.SetPT) {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			final int charges = permanent.getCounters(MagicCounterType.Charge);
			if (charges >= 12) {
				pt.set(9,9);
			} else if (charges >= 6) {
				pt.set(6,6);
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
			if (charges >= 12) {
				return flags |
					MagicAbility.LifeLink.getMask() |
					MagicAbility.Indestructible.getMask();
			} else if (charges >= 6) {
				return flags | MagicAbility.LifeLink.getMask();
			} else {
                return flags;
            }
		}
    };

	public static final MagicPermanentActivation A1 = new MagicLevelUpActivation(MagicManaCost.ONE,12);
}
