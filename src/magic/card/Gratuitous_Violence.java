package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Gratuitous_Violence {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.IfDamageWouldBeDealt,3) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicDamage damage=(MagicDamage)data;
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
