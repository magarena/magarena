package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenLifeIsGainedTrigger;

public class Sanguine_Bond {
    public static final MagicWhenLifeIsGainedTrigger T = new MagicWhenLifeIsGainedTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object[] data) {
			final MagicPlayer player = permanent.getController();
			final int amount = (Integer)data[1];
			return (player == (MagicPlayer)data[0]) ?
				new MagicEvent(
                    permanent,
                    player,
                    new Object[]{game.getOpponent(player),amount},
                    this,
                    game.getOpponent(player) + " loses " + amount + " life."):
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
					-(Integer)data[1]));
		}
    };
}
