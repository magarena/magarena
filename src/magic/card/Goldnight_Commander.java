package magic.card;

import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Goldnight_Commander {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPermanent otherPermanent) {
			final MagicPlayer player = permanent.getController();
			return (otherPermanent != permanent &&
                    otherPermanent.getController() == player &&
                    otherPermanent.isCreature()) ?
                new MagicEvent(
                        permanent,
                        player,
                        MagicEvent.NO_DATA,
                        this,
                        "Creatures " + player + " controls get +1/+1 until end of turn."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final Collection<MagicTarget> targets = game.filterTargets(
					event.getPlayer(),
					MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				game.doAction(new MagicChangeTurnPTAction((MagicPermanent)target,1,1));
			}
		}		
    };
}
