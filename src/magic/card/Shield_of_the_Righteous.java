package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.MagicChangeStateAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class Shield_of_the_Righteous {
    public static final MagicWhenBlocksTrigger T = new MagicWhenBlocksTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
			final MagicPermanent equippedCreature = permanent.getEquippedCreature();
			final MagicPermanent blocked = equippedCreature.getBlockedCreature();
			return (equippedCreature == data && blocked.isValid()) ?
		        new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{blocked},
                    this,
                    blocked + " doesn't untap during its controller's next untap step.") :
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeStateAction((MagicPermanent)data[0],MagicPermanentState.DoesNotUntap,true));
		}
    };
}
