package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Canker_Abomination {
	public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPlayer player) {
			final int amount = game.getOpponent(player).getNrOfPermanentsWithType(MagicType.Creature);
			return (amount > 0) ?
				new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent,amount},
                    this,
                    amount > 1 ?
                    		permanent + " enters the battlefield with " +
                    			amount + " -1/-1 counters on it." :
                    		permanent + " enters the battlefield with a " +
                    				"-1/-1 counter on it.") :
               MagicEvent.NONE;
		}

		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object data[],
				final Object[] choiceResults) {
			game.doAction(new MagicChangeCountersAction(
					(MagicPermanent)data[0],
					MagicCounterType.MinusOne,
					(Integer)data[1],
					true));
		}
		
		@Override
		public boolean usesStack() {
			return false;
		}
    };
}
