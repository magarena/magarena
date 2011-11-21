package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicManaType;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicChangeCountersAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Gavony_Township {
    public static final MagicPermanentActivation A = new MagicPermanentActivation( 
			new MagicCondition[]{
				MagicCondition.CAN_TAP_CONDITION,
				MagicManaCost.TWO_GREEN_WHITE.getCondition()
            },
            new MagicActivationHints(MagicTiming.Pump),
            "Pump") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
					new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.TWO_GREEN_WHITE)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source.getController()},
                    this,
                    "Put a +1/+1 counter on each creature you control.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPlayer player = (MagicPlayer)data[0];
			final Collection<MagicTarget> targets =
                game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
					game.doAction(new MagicChangeCountersAction(
							(MagicPermanent)target,MagicCounterType.PlusOne,1,true));
			}
		}
	};
}
