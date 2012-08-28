package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicAbility;
import magic.model.condition.MagicCondition;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Etched_Champion {
    public static final MagicStatic S = new MagicStatic(MagicLayer.Ability) {
        @Override
        public long getAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final long flags) {
            return MagicCondition.METALCRAFT_CONDITION.accept(permanent) ?
                flags | MagicAbility.ProtectionFromAllColors.getMask() :
                flags;
        }
    };
}
