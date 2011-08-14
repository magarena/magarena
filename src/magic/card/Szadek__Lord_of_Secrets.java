package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicMillLibraryAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Szadek__Lord_of_Secrets {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.IfDamageWouldBeDealt,6) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicDamage damage=(MagicDamage)data;
			final int amount=damage.getAmount();
			if (amount>0 && 
                damage.isCombat() && 
                permanent==damage.getSource() && 
                damage.getTarget().isPlayer()) {
				// Replacement effect.
				damage.setAmount(0);
				game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.PlusOne,amount,true));
				game.doAction(new MagicMillLibraryAction((MagicPlayer)damage.getTarget(),amount));
			}			
			return null;
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
