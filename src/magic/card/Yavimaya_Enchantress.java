package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.target.MagicTargetFilter;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicLayer;

public class Yavimaya_Enchantress {
	public static final MagicStatic S = new MagicStatic(MagicLayer.ModPT) {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			final int size = game.filterTargets(permanent.getController(),MagicTargetFilter.TARGET_ENCHANTMENT).size();
			pt.add(size,size);
		}
	};
}
