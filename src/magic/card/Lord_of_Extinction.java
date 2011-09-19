package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicCDA;

public class Lord_of_Extinction {
	public static final MagicCDA CDA = new MagicCDA() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
			final int amount=game.getPlayer(0).getGraveyard().size()+game.getPlayer(1).getGraveyard().size();
			pt.set(amount,amount);
		}
	};
}
