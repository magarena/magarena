package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtEndOfTurnTrigger;

public class Demonic_Rising {
    public static final MagicAtEndOfTurnTrigger T = new MagicAtEndOfTurnTrigger() {
		@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPlayer data) {
			final MagicPlayer player = permanent.getController();
			return (player == data &&
					player.getNrOfPermanentsWithType(MagicType.Creature) == 1) ?
				new MagicEvent(
                    permanent,
                    player,
                    MagicEvent.NO_DATA,
                    this,
                    player + " puts a 5/5 black Demon creature " +
                    "token with flying onto the battlefield."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (event.getPlayer().getNrOfPermanentsWithType(MagicType.Creature) == 1) {
            	game.doAction(new MagicPlayTokenAction(
            			event.getPlayer(),
                		TokenCardDefinitions.get("Demon5")));
            }
		}
    };
}
