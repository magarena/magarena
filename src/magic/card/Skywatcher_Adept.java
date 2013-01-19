package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

import java.util.Set;

public class Skywatcher_Adept {
    public static final MagicStatic S1 = new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int charges = permanent.getCounters(MagicCounterType.Charge);
            if (charges >= 3) {
                pt.set(4,2);
            } else if (charges >= 1) {
                pt.set(2,2);
            }
        }        
    };
    
    public static final MagicStatic S2 = new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final Set<MagicAbility> flags) {
            if (permanent.getCounters(MagicCounterType.Charge) >= 1) {
                flags.add(MagicAbility.Flying);
            } 
        }
    };
}
