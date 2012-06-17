package magic.card;

import magic.model.MagicGame;
import magic.model.mstatic.MagicLayer;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Gavony_Ironwright {
    public static final MagicStatic S = new MagicStatic(
            MagicLayer.ModPT, 
            MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
        @Override
        public void modPowerToughness(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
                    pt.add(1,4);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target && source.getController().getLife() <= 5;
        }
    };
}
