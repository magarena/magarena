package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Subversion {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
		@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPlayer data) {
			final MagicPlayer player = permanent.getController();
			return (player == data) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                		"Your opponent loses 1 life. You gain 1 life."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer player = (MagicPlayer)data[0];
			game.doAction(new MagicChangeLifeAction(game.getOpponent(player),-1));
			game.doAction(new MagicChangeLifeAction(player,1));
		}		
    };
}
