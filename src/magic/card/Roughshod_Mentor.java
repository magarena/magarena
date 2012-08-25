package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicLayer;
import magic.model.MagicAbility;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Roughshod_Mentor {
    public static final MagicStatic S = new MagicStatic(
        MagicLayer.Ability, 
        MagicTargetFilter.TARGET_GREEN_CREATURE_YOU_CONTROL) {
        @Override
        public long getAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final long flags) {
            return flags | MagicAbility.Trample.getMask();
        }
    };
    
}
