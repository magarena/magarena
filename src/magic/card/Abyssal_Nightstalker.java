package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicIfDamageWouldBeDealtTrigger;

public class Abyssal_Nightstalker {
	public static final MagicIfDamageWouldBeDealtTrigger T = new MagicIfDamageWouldBeDealtTrigger(1) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			return (damage.getSource() == permanent &&
				damage.isCombat() &&
				damage.getTarget().isPlayer() &&
				!permanent.isBlocked()) ?
					new MagicEvent(
						permanent,
						permanent.getController(),
						new Object[]{permanent,damage.getTarget()},
						this,
						damage.getTarget() + " discards a card.") :
			MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.addEvent(new MagicDiscardEvent(
					(MagicPermanent)data[0],
					(MagicPlayer)data[1],
					1,
					false));
		}
    };
}
