package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Caravan_Escort {
    public static final MagicStatic S1 = new MagicStatic(MagicLayer.SetPT) {
		@Override
		public void getPowerToughness(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
			final int charges = permanent.getCounters(MagicCounterType.Charge);
			if (charges >= 5) {
				pt.set(5,5);
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
			if (permanent.getCounters(MagicCounterType.Charge) >= 5) {
				return flags|MagicAbility.FirstStrike.getMask();
			}
			return flags;
		}
    };
}
