package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicBounceTargetPicker;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Shivan_Wurm {
	public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenComesIntoPlay) {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			return new MagicEvent(
                    permanent,
                    permanent.getController(),
                    MagicTargetChoice.RED_OR_GREEN_CREATURE_YOU_CONTROL,
                    MagicBounceTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Return a red or green creature you control$ to its owner's hand.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.OwnersHand));
                }
			});
		}
    };
}
