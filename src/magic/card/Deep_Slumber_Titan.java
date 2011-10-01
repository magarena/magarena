package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicUntapAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTappedIntoPlayTrigger;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Deep_Slumber_Titan {
    public static final MagicWhenDamageIsDealtTrigger T1 = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			return (damage.getTarget() == permanent &&
					permanent.isTapped()) ?
					new MagicEvent(
						permanent,
						permanent.getController(),
						new Object[]{permanent},
						this,
						"Untap " + permanent + ".") :
					MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object data[],
				final Object[] choiceResults) {
			game.doAction(new MagicUntapAction((MagicPermanent)data[0]));
		}
    };
    
    public static final MagicTrigger T2 = new MagicTappedIntoPlayTrigger();
}
