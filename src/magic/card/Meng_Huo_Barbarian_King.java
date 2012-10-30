package magic.card;

import magic.model.MagicPowerToughness;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Meng_Huo_Barbarian_King {
    public static final MagicStatic S2 = new MagicStatic(
        MagicLayer.ModPT,
        MagicTargetFilter.TARGET_GREEN_CREATURE_YOU_CONTROL) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,1);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target;
        }
    };
}
