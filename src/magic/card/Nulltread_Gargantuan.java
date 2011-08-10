package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicBounceTargetPicker;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Nulltread_Gargantuan {

	public static final MagicTrigger V8336 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Nulltread Gargantuan") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.CREATURE_YOU_CONTROL,
				MagicBounceTargetPicker.getInstance(),MagicEvent.NO_DATA,this,"Put a creature you control$ on top of its owner's library.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.TopOfOwnersLibrary));
			}
		}
    };
    
}
