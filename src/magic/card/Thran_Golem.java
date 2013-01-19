package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

import java.util.Set;

public class Thran_Golem {
    public static final MagicStatic S1 = new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (permanent.isEnchanted()) {
                pt.add(2,2);
            }
        }    
    };
    
    public static final MagicStatic S2 = new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (permanent.isEnchanted()) {
                flags.add(MagicAbility.Flying);
                flags.add(MagicAbility.FirstStrike);
                flags.add(MagicAbility.Trample);
            }
        }
    };
}
