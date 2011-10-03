package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicRegenerateAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicSingleActivationCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicTiming;

public class Corrupted_Harvester {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
			new MagicCondition[]{
				MagicManaCost.BLACK.getCondition(),
                MagicCondition.ONE_CREATURE_CONDITION,
                MagicCondition.CAN_REGENERATE_CONDITION,
                new MagicSingleActivationCondition()
            },
            new MagicActivationHints(MagicTiming.Pump),
            "Regen") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
					new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.BLACK),
					new MagicSacrificePermanentEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.SACRIFICE_CREATURE)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
	                source,
	                source.getController(),
	                new Object[]{source},
	                this,
	                "Regenerate " + source + ".");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicRegenerateAction((MagicPermanent)data[0]));
		}
	};
}
