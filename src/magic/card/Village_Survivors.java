package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

import java.util.Set;

public class Village_Survivors {
    public static final MagicStatic S = new MagicStatic(
            MagicLayer.Ability, 
            MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            if (permanent.getController().getLife() <= 5) {
                flags.add(MagicAbility.Vigilance);
            }
        }
    };
}
