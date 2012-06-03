package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class Meglonoth {
    public static final MagicWhenBlocksTrigger T = new MagicWhenBlocksTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            final MagicPermanent blocked=permanent.getBlockedCreature();
			return (permanent==data && blocked.isValid()) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{permanent,blocked.getController()},
                        this,
                        permanent + " deals damage to the blocked creature's controller equal to " +
                        permanent + "'s power."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicDamage damage=new MagicDamage(permanent,(MagicTarget)data[1],permanent.getPower(),false);
			game.doAction(new MagicDealDamageAction(damage));
		}
    };
}
