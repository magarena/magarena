package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicPumpTargetPicker;

public class Deranged_Outcast {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
			new MagicCondition[]{
	                MagicManaCost.ONE_GREEN.getCondition(),
	                MagicCondition.ONE_CREATURE_CONDITION,
	            },
            new MagicActivationHints(MagicTiming.Removal),
            "Destroy") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
					new MagicPayManaCostEvent(
							source,
							source.getController(),
							MagicManaCost.ONE_GREEN),
					new MagicSacrificePermanentEvent(
							source,
							source.getController(),
							MagicTargetChoice.SACRIFICE_HUMAN)};
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
                    source.getController() + " puts two +1/+1 counters on target creature$.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                	game.doAction(new MagicChangeCountersAction(
        					creature,
        					MagicCounterType.PlusOne,
        					2,
        					true));
                }
			});
		}
	};
}
