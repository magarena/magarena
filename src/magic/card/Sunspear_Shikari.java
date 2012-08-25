
package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Sunspear_Shikari {
    public static final MagicStatic S = new MagicStatic(MagicLayer.Ability) {
        @Override
        public long getAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final long flags) {
            return permanent.isEquipped() ? 
                flags | 
                MagicAbility.FirstStrike.getMask() |
                MagicAbility.LifeLink.getMask()
                :
                flags;
        }
    };
}
