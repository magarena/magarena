package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Rabid_Elephant {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            if (data == permanent) {
            	final int amount = permanent.getBlockingCreatures().size() * 2;
            	return new MagicEvent(
            			permanent,
            			permanent.getController(),
            			new Object[]{permanent,amount},
            			this,
            			permanent + " gets +" + amount + "/+" +  amount + " until end of turn.");
            }
            return MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeTurnPTAction(
					(MagicPermanent)data[0],
					(Integer)data[1],
					(Integer)data[1]));
		}
    };
}
