package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicActivation;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificeEvent;
import magic.model.event.MagicTiming;

public class Gristleback {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
            MagicActivation.NO_COND,
            new MagicActivationHints(MagicTiming.Removal),
            "Life") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}
		@Override
		public MagicEvent getPermanentEvent(
				final MagicPermanent source,
				final MagicPayedCost payedCost) {
			final MagicPlayer player = source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player,source},
                    this,
                    player + " gains life equal to " + source + "'s power.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent permanent = (MagicPermanent)data[1];
			game.doAction(new MagicChangeLifeAction(
					(MagicPlayer)data[0],
					permanent.getPower(game)));
		}
	};
}
