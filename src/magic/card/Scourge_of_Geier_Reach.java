package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicType;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicLayer;

public class Scourge_of_Geier_Reach {
	public static final MagicStatic S = new MagicStatic(MagicLayer.ModPT) {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			final int amount = game.getOpponent(permanent.getController()).getNrOfPermanentsWithType(MagicType.Creature,game);
			pt.add(amount,amount);
		}
	};
}
