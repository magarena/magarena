package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

import java.util.Set;

public class Lord_of_the_Unreal {
    public static final MagicStatic S = new MagicStatic(
        MagicLayer.ModPT, 
        MagicTargetFilter.TARGET_ILLUSION_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,1);
        }
    };
    public static final MagicStatic S2 = new MagicStatic(
        MagicLayer.Ability, 
        MagicTargetFilter.TARGET_ILLUSION_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            flags.add(MagicAbility.Hexproof);
        }
    };
}
