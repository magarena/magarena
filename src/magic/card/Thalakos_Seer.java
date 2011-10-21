package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Thalakos_Seer {
    public static final MagicWhenLeavesPlayTrigger T = new MagicWhenLeavesPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent data) {
			final MagicPlayer player = permanent.getController();
			return (permanent == data) ?
					new MagicEvent(
							permanent,
							player,
							new Object[]{player},
							this,
							player + " draws a card.") :
					MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicDrawAction((MagicPlayer)data[0],1));
		}
    };
}
