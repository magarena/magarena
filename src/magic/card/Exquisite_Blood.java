package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenLifeIsLostTrigger;

public class Exquisite_Blood {
    public static final MagicWhenLifeIsLostTrigger T = new MagicWhenLifeIsLostTrigger() {
		@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final Object[] data) {
			final MagicPlayer player = permanent.getController();
			final int amount = (Integer)data[1];
			return (game.getOpponent(player) == (MagicPlayer)data[0]) ?
				new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player,amount},
                    this,
                    player + " gains " + amount + " life."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeLifeAction(
					(MagicPlayer)data[0],
					(Integer)data[1]));
		}
    };
}
