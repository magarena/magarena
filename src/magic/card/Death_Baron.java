package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.MagicSubType;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Death_Baron {
    public static final MagicStatic S = new MagicStatic(
        MagicLayer.ModPT, 
        MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
        @Override
        public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,1);
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return filter.accept(game, source.getController(), target) &&
                   (target.hasSubType(MagicSubType.Skeleton) || 
                    (source != target && target.hasSubType(MagicSubType.Zombie)));
        }
    };
}
