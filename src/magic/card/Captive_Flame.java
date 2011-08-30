package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicPumpTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.action.MagicPermanentAction;

public class Captive_Flame {

	public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicManaCost.RED.getCondition()},
            new MagicActivationHints(MagicTiming.Pump,true),
            "Pump") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.RED)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicPumpTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Target creature$ gets +1/+0 until end of turn.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,1,0));
                }
			});
		}
	};
}
