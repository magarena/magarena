package magic.card;

import magic.model.MagicColor;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicIfDamageWouldBeDealtTrigger;

public class Fire_Servant {
    public static final MagicIfDamageWouldBeDealtTrigger T = new MagicIfDamageWouldBeDealtTrigger(3) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			final MagicSource source=damage.getSource();
			if (source.getController()==permanent.getController()&&source.isSpell()&&
				MagicColor.Red.hasColor(source.getColorFlags(game))&&source.getCardDefinition().isSpell()) {
				// Generates no event or action.
				damage.setAmount(damage.getAmount()<<1);
			}			
			return MagicEvent.NONE;
		}
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
		}
    };
}
