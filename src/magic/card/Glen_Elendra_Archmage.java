package magic.card;

import magic.model.*;
import magic.model.action.MagicCounterItemOnStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.stack.MagicCardOnStack;
import magic.model.action.MagicCardOnStackAction;

public class Glen_Elendra_Archmage {

	public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicManaCost.BLUE.getCondition()},
            new MagicActivationHints(MagicTiming.Counter),
            "Counter") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostSacrificeEvent(source,source.getController(),MagicManaCost.BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_NONCREATURE_SPELL,
                    MagicEvent.NO_DATA,
                    this,
                    "Counter target noncreature spell$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			event.processTargetCardOnStack(game,choiceResults,0,new MagicCardOnStackAction() {
                public void doAction(final MagicCardOnStack targetSpell) {
                    game.doAction(new MagicCounterItemOnStackAction(targetSpell));
                }
			});
		}
	};
}
