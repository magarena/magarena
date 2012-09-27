package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesTappedTrigger;

public class City_of_Brass {
	
	public static final MagicWhenBecomesTappedTrigger T = new MagicWhenBecomesTappedTrigger() {
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent tapped) {
			return (permanent==tapped) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN deals 1 damage to you."
                ) :
                MagicEvent.NONE;
		}

		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object data[],
				final Object[] choiceResults) {
			final MagicDamage damage=new MagicDamage(event.getSource(),event.getPlayer(),1,false);
			game.doAction(new MagicDealDamageAction(damage));
		}
	};
}
