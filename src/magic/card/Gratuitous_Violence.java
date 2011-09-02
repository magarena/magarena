package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicIfDamageWouldBeDealtTrigger;

public class Gratuitous_Violence {
    public static final MagicIfDamageWouldBeDealtTrigger T = new MagicIfDamageWouldBeDealtTrigger(3) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			final MagicSource source=damage.getSource();
			if (source.getController()==permanent.getController()&&source.isPermanent()) {
				final MagicPermanent sourcePermanent=(MagicPermanent)source;
				if (sourcePermanent.isCreature()) {
					// Generates no event or action.
					damage.setAmount(damage.getAmount()<<1);
				}
			}			
			return null;
		}
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
		}
    };
}
