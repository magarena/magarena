package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Berserk_Murlodont {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
			final MagicPlayer player = permanent.getController();
            if (player == data.getController() &&
            	data.hasSubType(MagicSubType.Beast)) {
            	final int amount = data.getBlockingCreatures().size();
            	return new MagicEvent(
            			permanent,
            			player,
            			new Object[]{data,amount},
            			this,
            			data + " gets +" + amount + "/+" +  amount + " until end of turn.");
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
