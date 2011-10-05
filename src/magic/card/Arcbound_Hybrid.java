package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicModularTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Arcbound_Hybrid {
    public static final MagicWhenComesIntoPlayTrigger T1 = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent},
                    this,
					permanent + " enters the battlefield with 2 +1/+1 counters on it.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,2,false));
		}
		@Override
		public boolean usesStack() {
			return false;
		}
    };
    
    public static final MagicModularTrigger T2 = new MagicModularTrigger();
}
