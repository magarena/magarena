package magic.card;

import magic.model.*;
import magic.model.action.MagicSacrificeAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Serrated_Arrows {

	public static final MagicPermanentActivation A1 = new MagicWeakenCreatureActivation(
			new MagicCondition[]{
                MagicCondition.CAN_TAP_CONDITION,
                MagicCondition.CHARGE_COUNTER_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "-1/-1") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPermanent permanent=(MagicPermanent)source;
			return new MagicEvent[]{
				new MagicTapEvent(permanent),
				new MagicRemoveCounterEvent(permanent,MagicCounterType.Charge,1)};
		}
	};

    public static final MagicTrigger T1 = new MagicTrigger(MagicTriggerType.WhenComesIntoPlay) {
		@Override
		public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final Object data) {
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent},
                    this,
					permanent + " enters the battlefield with three arrowhead counters on it.");
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeCountersAction(
                        (MagicPermanent)data[0],
                        MagicCounterType.Charge,
                        3,
                        false));
		}

		@Override
		public boolean usesStack() {
			return false;
		}
    };

    public static final MagicTrigger T2 = new MagicTrigger(MagicTriggerType.AtUpkeep) {
		@Override
		public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final Object data) {
			final MagicPlayer player=permanent.getController();
			if (player == data && permanent.getCounters(MagicCounterType.Charge) == 0) {
				return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent},
                        this,
                        "Sacrifice " + permanent + ".");
			}
			return null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicSacrificeAction((MagicPermanent)data[0]));
		}
    };
}
