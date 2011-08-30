package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDestroyAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.action.MagicPermanentAction;

public class Visara_the_Dreadful {

	public static final MagicPermanentActivation A = new MagicPermanentActivation(
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Destroy") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicDestroyTargetPicker(true),
				    MagicEvent.NO_DATA,
                    this,
                    "Destroy target creature$. It can't be regenerated.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.CannotBeRegenerated,true));
                    game.doAction(new MagicDestroyAction(creature));
                }
			});
		}
	};
}
