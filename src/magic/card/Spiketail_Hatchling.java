package magic.card;

import magic.model.*;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.*;
import magic.model.stack.MagicCardOnStack;
import magic.model.condition.MagicCondition;
import magic.model.action.MagicCardOnStackAction;

public class Spiketail_Hatchling {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
            MagicCondition.NONE,
            new MagicActivationHints(MagicTiming.Counter),
            "Counter") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_SPELL,
                    new Object[]{source},
                    this,
                    "Counter target spell$ unless its controller pays {1}.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
            event.processTargetCardOnStack(game,choiceResults,0,new MagicCardOnStackAction() {
                public void doAction(final MagicCardOnStack targetSpell) {
                    game.addEvent(new MagicCounterUnlessEvent((MagicSource)data[0],targetSpell,MagicManaCost.ONE));
                }
			});
		}
	};
}
