package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.target.MagicPumpTargetPicker;

public class Slayers__Stronghold {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
			new MagicCondition[]{
				MagicCondition.CAN_TAP_CONDITION,
				MagicManaCost.RED_WHITE.getCondition()
			},
            new MagicActivationHints(MagicTiming.Pump),
            "Pump") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
					new MagicPayManaCostTapEvent(
							source,
							source.getController(),
							MagicManaCost.RED_WHITE)
			};
		}

		@Override
		public MagicEvent getPermanentEvent(
				final MagicPermanent source,
				final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicPumpTargetPicker.create(),
                    MagicEvent.NO_DATA,
                    this,
                    "Target creature gets +2/+0 and gains " +
                    "vigilance and haste until end of turn.");
		}

		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object[] data,
				final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,2,0));
                    game.doAction(new MagicSetAbilityAction(
                    		creature,
                    		MagicAbility.Vigilance));
                    game.doAction(new MagicSetAbilityAction(
                    		creature,
                    		MagicAbility.Haste));
                }
			});
		}
	};
}
