package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicIfPlayerWouldLoseTrigger;

public class Platinum_Angel {
    public static final MagicIfPlayerWouldLoseTrigger T = new MagicIfPlayerWouldLoseTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer[] playerRef) {
			final MagicPlayer controller = permanent.getController();
            if (controller == playerRef[0]) {
                playerRef[0] = MagicPlayer.NONE;
			}			
			return MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
		}
    };
}
