package magic.card;

import magic.model.*;
import magic.model.action.MagicMillLibraryAction;
import magic.model.event.*;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Belltower_Sphinx {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenDamageIsDealt) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicDamage damage = (MagicDamage)data;
            final int amount = damage.getDealtAmount();
            if (damage.getTarget() == permanent) {
            	game.doAction(new MagicMillLibraryAction(damage.getSource().getController(),amount));
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
