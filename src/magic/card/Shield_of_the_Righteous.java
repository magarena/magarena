package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.MagicChangeStateAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Shield_of_the_Righteous {

    public static final MagicTrigger V9645 =new MagicTrigger(MagicTriggerType.WhenBlocks,"Shield of the Righteous") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPermanent equippedCreature=permanent.getEquippedCreature();
			if (equippedCreature==data&&equippedCreature!=null) {
				final MagicPermanent blocked=equippedCreature.getBlockedCreature();
				if (blocked!=null) {
					return new MagicEvent(permanent,permanent.getController(),new Object[]{blocked},this,
							blocked.getName()+" doesn't untap during its controller's next untap step.");
				}
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeStateAction((MagicPermanent)data[0],MagicPermanentState.DoesNotUntap,true));
		}
    };
    
}
