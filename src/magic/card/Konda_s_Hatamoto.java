package magic.card;

import java.util.Collection;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

public class Konda_s_Hatamoto {
    
    public static final MagicStatic S1 = new MagicStatic(MagicLayer.Ability) {
        @Override
        public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
            return game.filterTargets(permanent.getController(),
                    MagicTargetFilter.TARGET_LEGENDARY_SAMURAI_YOU_CONTROL).size() > 0 ?
                flags | MagicAbility.Vigilance.getMask() :
                flags;
        }
    };
    
    public static final MagicStatic S2 = new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final Collection<MagicTarget> targets =
                    game.filterTargets(permanent.getController(),MagicTargetFilter.TARGET_LEGENDARY_SAMURAI_YOU_CONTROL);
            if (targets.size() > 0) {
                pt.add(1,2);
            }        
        }
    };
}
