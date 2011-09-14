package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Veteran_Armorer {
    public static final MagicStatic S = new MagicStatic(
        MagicLayer.ModPT, 
        MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
        @Override
        public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.toughness += 1;
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target && filter.accept(game, source.getController(), target);
        }
    };
}
