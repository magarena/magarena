package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSubType;
import magic.model.MagicAbility;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Angelic_Overseer {
    public static final MagicStatic S = new MagicStatic(MagicLayer.Ability) {
        @Override
        public long getAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final long flags) {
            for (final MagicPermanent target : permanent.getController().getPermanents()) {
                if (target != permanent && target.hasSubType(MagicSubType.Human)) {
                    return flags | 
                            MagicAbility.CannotBeTheTarget.getMask() |
                            MagicAbility.Indestructible.getMask();
                }
            }
            return flags;
        }
    };
}
