package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenLifeIsGainedTrigger;

public class Drogskol_Reaver {
    public static final MagicWhenLifeIsGainedTrigger T = new MagicWhenLifeIsGainedTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object[] data) {
			final MagicPlayer player = permanent.getController();
			return (player == (MagicPlayer)data[0]) ?
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
