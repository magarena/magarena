package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSubType;
import magic.model.MagicAbility;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Night_Revelers {
    public static final MagicStatic S = new MagicStatic(MagicLayer.Ability) {
        @Override
        public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
            for (final MagicPermanent target : game.getOpponent(permanent.getController()).getPermanents()) {
                if (target != permanent && target.hasSubType(MagicSubType.Human)) {
                    return flags | MagicAbility.Haste.getMask();
                }
            }
            return flags;
        }
    };
}
