package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.target.MagicTargetFilter;
import magic.model.mstatic.MagicCDA;

public class Magnivore {
	public static final MagicCDA CDA = new MagicCDA() {
		@Override
		public void getPowerToughness(
				final MagicGame game,
				final MagicPlayer player,
				final MagicPowerToughness pt) {
			final int size = game.filterTargets(
					player,
					MagicTargetFilter.TARGET_SORCERY_CARD_FROM_ALL_GRAVEYARDS).size();
			pt.set(size, size);
		}
	};
}
