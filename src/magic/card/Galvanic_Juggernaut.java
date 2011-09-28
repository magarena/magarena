package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicUntapAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;

public class Galvanic_Juggernaut {
    public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
			return (permanent != otherPermanent &&
					otherPermanent.isCreature(game) &&
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
}
