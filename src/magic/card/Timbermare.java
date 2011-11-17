package magic.card;

import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicTapAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicEchoTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Timbermare {
	public static final MagicWhenComesIntoPlayTrigger T2 = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPlayer player) {
			return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent},
                    this,
                    "Tap all other creatures.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final Collection<MagicTarget> targets = game.filterTargets(
					game.getPlayer(0),
					MagicTargetFilter.TARGET_CREATURE);
			for (final MagicTarget target : targets) {
				final MagicPermanent creature = (MagicPermanent)target;
				if (creature != (MagicPermanent)data[0]) {
					game.doAction(new MagicTapAction(creature,true));
				}
			}
		}
    };
}
