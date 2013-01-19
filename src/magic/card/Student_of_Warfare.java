package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

import java.util.Set;

public class Student_of_Warfare {
    public static final MagicStatic S1 = new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            final int charges=permanent.getCounters(MagicCounterType.Charge);
            if (charges>=7) {
                pt.set(4,4);
            } else if (charges>=2) {
                pt.set(3,3);
            }
        }
    };

    public static final MagicStatic S2 = new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final Set<MagicAbility> flags) {
            final int charges=permanent.getCounters(MagicCounterType.Charge);
            if (charges>=7) {
                flags.add(MagicAbility.DoubleStrike);
            } else if (charges>=2) {
                flags.add(MagicAbility.FirstStrike);
            }
        }
    };
}
