package magic.card;

import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRegenerationActivation;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;

import java.util.Collection;

public class Mortivore {
    //Characteristic defining ability
    //IMPT: permanent is MagicPermanent.NONE
	public static final MagicLocalVariable LV = new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			final Collection<MagicTarget> targets =
                    game.filterTargets(game.getPlayer(0),MagicTargetFilter.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS);
			pt.set(targets.size(), targets.size());
		}
	};
    
    public static final MagicPermanentActivation A = new MagicRegenerationActivation(MagicManaCost.BLACK);
}
