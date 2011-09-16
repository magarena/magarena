package magic.card;

import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;

public class Lord_of_Extinction {
    //Characteristic defining ability
	public static final MagicLocalVariable LORD_OF_EXTINCTION=new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			final int amount=game.getPlayer(0).getGraveyard().size()+game.getPlayer(1).getGraveyard().size();
			pt.set(amount,amount);
		}
	};
}
