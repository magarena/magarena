package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Hada_Spy_Patrol {
    public static final MagicStatic S1 = new MagicStatic(MagicLayer.SetPT) {
		@Override
		public void getPowerToughness(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
			final int charges = permanent.getCounters(MagicCounterType.Charge);
			if (charges >= 3) {
				pt.set(3,3);
			} else if (charges >= 1) {
				pt.set(2,2);
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
			if (charges >= 3) {
				return flags |
						MagicAbility.Shroud.getMask() |
						MagicAbility.Unblockable.getMask();
			} else if (charges >= 1) {
				return flags|MagicAbility.Unblockable.getMask();
			}
			return flags;
		}
    };
}
