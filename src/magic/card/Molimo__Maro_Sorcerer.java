package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicType;
import magic.model.mstatic.MagicCDA;

public class Molimo__Maro_Sorcerer {
	public static final MagicCDA CDA = new MagicCDA() {
		@Override
		public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
			final int size = player.getNrOfPermanentsWithType(MagicType.Land);		
			pt.set(size, size);
		}
	};
}
