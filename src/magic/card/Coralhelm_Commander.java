package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.mstatic.MagicLayer;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Coralhelm_Commander {
    public static final MagicStatic S = new MagicStatic(
            MagicLayer.ModPT, 
            MagicTargetFilter.TARGET_MERFOLK_YOU_CONTROL) {

        @Override
        public void modPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,1);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getCounters(MagicCounterType.Charge) >= 4 && source != target;
        }
    };
        
    public static final MagicStatic S1 = new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            final int charges = permanent.getCounters(MagicCounterType.Charge);
            if (charges >= 4) {
                pt.set(4,4);
            } else if (charges >= 2) {
                pt.set(3,3);
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
            return (charges >= 2) ?
                flags|MagicAbility.Flying.getMask():
                flags;
        }
    };
}
