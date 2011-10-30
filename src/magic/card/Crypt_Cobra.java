package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangePoisonAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicIfDamageWouldBeDealtTrigger;

public class Crypt_Cobra {
	public static final MagicIfDamageWouldBeDealtTrigger T = new MagicIfDamageWouldBeDealtTrigger(1) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			if (damage.getSource() == permanent &&
				damage.isCombat() &&
				damage.getTarget().isPlayer() &&
				!permanent.isBlocked()) {
				return new MagicEvent(
						permanent,
						permanent.getController(),
						new Object[]{damage.getTarget()},
						this,
	                    damage.getTarget() + " gets a poison counter.");
            }
            return MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangePoisonAction((MagicPlayer)data[0],1));
		}
    };
}
