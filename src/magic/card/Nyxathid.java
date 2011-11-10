package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicLayer;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Nyxathid {
	public static final MagicStatic S = new MagicStatic(MagicLayer.ModPT) {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			final MagicPlayer opponent = (MagicPlayer)permanent.getChosenTarget();
			final int amount = opponent.getHandSize();
			pt.add(-amount,-amount);
		}
	};
	
	public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPlayer player) {
			permanent.setChosenTarget(game.getOpponent(player));
			return MagicEvent.NONE;
		}

		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object data[],
				final Object[] choiceResults) {
		}
    };
}
