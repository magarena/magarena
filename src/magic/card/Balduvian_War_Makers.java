package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Balduvian_War_Makers {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
			final MagicPlayer player = permanent.getController();
            if (creature == permanent) {
            	final MagicPermanentList plist = permanent.getBlockingCreatures();
            	final int amount = 1 * (plist.size() - 1);
            	if (amount > 0) {
            		return new MagicEvent(
                            permanent,
                            player,
                            new Object[]{permanent,amount},
                            this,
                            permanent + " gets +" + amount + "/+" +  amount + " until end of turn.");
            	}
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
