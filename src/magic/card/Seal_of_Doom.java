package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDestroyAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.*;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.condition.MagicCondition;
import magic.model.action.MagicPermanentAction;

public class Seal_of_Doom {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
            MagicCondition.NONE,
            new MagicActivationHints(MagicTiming.Removal),
            "Destroy") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_NONBLACK_CREATURE,
                    new MagicDestroyTargetPicker(true),
                    MagicEvent.NO_DATA,
                    this,
                    "Destroy target nonblack creature$. It can't be regenerated.");
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
