package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import java.util.Collection;

public class Loam_Lion {
    public static final MagicStatic S = new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final Collection<MagicTarget> targets =
                    game.filterTargets(permanent.getController(),MagicTargetFilter.TARGET_FOREST_YOU_CONTROL);
            if (targets.size() > 0) {
                pt.add(1,2);
            }        
        }
    };
}
