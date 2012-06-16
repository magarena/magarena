package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Somberwald_Vigilante {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
		@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPermanent creature) {
            if (creature == permanent) {
            	final MagicPermanentList plist = permanent.getBlockingCreatures();
            	return new MagicEvent(
            			permanent,
            			permanent.getController(),
            			new Object[]{permanent,plist},
            			this,
            			permanent + " deals 1 damage to blocking creature.");
            }
            return MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPermanent permanent = (MagicPermanent)data[0];
			final MagicPermanentList plist = (MagicPermanentList)data[1];
			for (final MagicPermanent blocker : plist) {
				final MagicDamage damage = new MagicDamage(permanent,blocker,1,false);
                game.doAction(new MagicDealDamageAction(damage));
        	}
		}
    };
}
