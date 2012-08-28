package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicAbility;
import magic.model.MagicPowerToughness;
import magic.model.condition.MagicCondition;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Auriok_Sunchaser {
    public static final MagicStatic S1 = new MagicStatic(MagicLayer.Ability) {
        @Override
        public long getAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final long flags) {
            return MagicCondition.METALCRAFT_CONDITION.accept(permanent) ?
                flags | MagicAbility.Flying.getMask() :
                flags;
        }
    };
    
    public static final MagicStatic S2 = new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            if (MagicCondition.METALCRAFT_CONDITION.accept(permanent)) {
                pt.add(2,2);
            }
        }
    };
}
