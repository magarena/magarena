package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

import java.util.Set;

public class Sensor_Splicer {
    public static final MagicStatic S = new MagicStatic(
            MagicLayer.Ability, 
            MagicTargetFilter.TARGET_GOLEM_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            flags.add(MagicAbility.Vigilance);
        }
    };
    
    public static final Object T = Blade_Splicer.T;
}
