package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicCounterItemOnStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Draining_Whelk {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenComesIntoPlay) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			return new MagicEvent(
                    permanent,
                    permanent.getController(),
                    MagicTargetChoice.TARGET_SPELL,
                    new Object[]{permanent},
                    this,
                    "Counter target spell$. Put X +1/+1 counters on " + permanent.getName() + 
                    ", where X is that spell's converted mana cost.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicCardOnStack card=event.getTarget(game,choiceResults,0);
			if (card!=null) {
				game.doAction(new MagicCounterItemOnStackAction(card));
				game.doAction(new MagicChangeCountersAction(
                            (MagicPermanent)data[0],
                            MagicCounterType.PlusOne,
                            card.getConvertedCost(),
                            true));
			}
		}
    };
}
