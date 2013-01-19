package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;
import java.util.Set;

public class Konda_s_Hatamoto {
    
    public static final MagicStatic S1 = new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            final MagicGame game = source.getGame();
            if (game.filterPermanents(permanent.getController(), MagicTargetFilter.TARGET_LEGENDARY_SAMURAI_YOU_CONTROL).size() > 0) {
                flags.add(MagicAbility.Vigilance);
            }
        }
    };
    
    public static final MagicStatic S2 = new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final MagicGame game = source.getGame();
            final Collection<MagicPermanent> targets =
                    game.filterPermanents(permanent.getController(),MagicTargetFilter.TARGET_LEGENDARY_SAMURAI_YOU_CONTROL);
            if (targets.size() > 0) {
                pt.add(1,2);
            }        
        }
    };
}
