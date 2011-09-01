package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicSacrificeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Arc_Runner {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.AtEndOfTurn) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player = permanent.getController();
			if (player == data) {
				return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent},
                        this,
                        "Sacrifice " + permanent + ".");
			}
			return null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicSacrificeAction((MagicPermanent)data[0]));
		}
    };
}
