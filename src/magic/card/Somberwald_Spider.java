package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Somberwald_Spider {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			if (game.getCreatureDiedThisTurn()) {
				game.doAction(new MagicChangeCountersAction(
						permanent,
						MagicCounterType.PlusOne,
						2,
						true));
				return new MagicEvent(
						permanent,
						player,
						new Object[]{permanent},
						this,
						permanent + " enters the battlefield with two +1/+1 counters on it ");
			}
			return MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
		}
    };
}
