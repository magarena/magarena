package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.target.MagicTargetFilter;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicLayer;

public class Squirrel_Mob {
    public static final MagicStatic S = new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            final int size = game.filterTargets(
                    permanent.getController(),
                    MagicTargetFilter.TARGET_SQUIRREL_CREATURE).size() - 1;
            pt.add(size,size);
        }
    };
}
