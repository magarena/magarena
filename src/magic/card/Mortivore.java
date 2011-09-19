package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicManaCost;
import magic.model.MagicPowerToughness;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRegenerationActivation;
import magic.model.target.MagicTargetFilter;
import magic.model.mstatic.MagicCDA;

import java.util.Collection;

public class Mortivore {
	public static final MagicCDA CDA = new MagicCDA() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
			final int size = 
                game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS).size();
			pt.set(size, size);
		}
	};
    
    public static final MagicPermanentActivation A = new MagicRegenerationActivation(MagicManaCost.BLACK);
}
