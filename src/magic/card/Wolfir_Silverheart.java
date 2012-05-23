package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.MagicAbility;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Wolfir_Silverheart {
    public static final MagicStatic S = new MagicStatic(
        MagicLayer.ModPT, 
        MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
        @Override
        public void getPowerToughness(final MagicGame game, final MagicPermanent permanent, final MagicPowerToughness pt) {
            pt.add(4,4);
        }
        @Override
		public boolean condition(
				final MagicGame game,
				final MagicPermanent source,
				final MagicPermanent target) {
        	return source.getPairedCreature() == target ||
        			(source == target && source.isPaired());
        }
    };
}
