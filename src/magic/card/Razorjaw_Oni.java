package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

import java.util.Set;

public class Razorjaw_Oni {
    public static final MagicStatic S = new MagicStatic(
        MagicLayer.Ability, 
        MagicTargetFilter.TARGET_BLACK_CREATURE) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            flags.add(MagicAbility.CannotBlock);
        }
    };
}
