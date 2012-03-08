package magic.card;

import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

public class Rabble_Rouser {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
			new MagicCondition[]{
					MagicCondition.CAN_TAP_CONDITION,
					MagicManaCost.RED.getCondition()},
            new MagicActivationHints(MagicTiming.Block),
            "Pump") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[] {new MagicPayManaCostTapEvent(
					source,
					source.getController(),
					MagicManaCost.RED)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Attacking creatures get +X/+0 until end of turn, " +
                    "where X is " + source + "'s power.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent permanent = (MagicPermanent)data[0];
			final Collection<MagicTarget> targets = game.filterTargets(
                    permanent.getController(),
                    MagicTargetFilter.TARGET_ATTACKING_CREATURE);
			for (final MagicTarget target : targets) {
				game.doAction(new MagicChangeTurnPTAction(
						(MagicPermanent)target,
						permanent.getPower(game),
						0));
			}
		}
	};
}
