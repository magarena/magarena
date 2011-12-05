package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Corrupt_Official {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
			final MagicPlayer defendingPlayer = game.getOpponent(permanent.getController());
			return (permanent == data ) ?
		            new MagicEvent(
		                    permanent,
		                    permanent.getController(),
		                    new Object[]{permanent,defendingPlayer},
		                    this,
		                    defendingPlayer + " discards a card at random."):
		            MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.addEvent(new MagicDiscardEvent(
					(MagicPermanent)data[0],
					(MagicPlayer)data[1],
					1,
					true));
		}
    };
}
