package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Guul_Draz_Specter {
	public static final MagicStatic S1 = new MagicStatic(MagicLayer.ModPT) {
		@Override
		public void modPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			if (game.getOpponent(permanent.getController()).getHand().isEmpty()) {
				pt.add(3,3);
			}
		}		
	};
}
