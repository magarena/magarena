package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicLayer;
import magic.model.MagicAbility;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Rhox_Pikemaster {
    public static final MagicStatic S = new MagicStatic(
        MagicLayer.Ability, 
        MagicTargetFilter.TARGET_SOLDIER_YOU_CONTROL) {
        @Override
        public long getAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final long flags) {
            return flags | MagicAbility.FirstStrike.getMask();
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target;
        }
    };
    
}
