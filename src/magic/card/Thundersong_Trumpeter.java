package magic.card;

import magic.model.*;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicNoCombatTargetPicker;

public class Thundersong_Trumpeter {

	public static final MagicPermanentActivation V1967 =new MagicPermanentActivation(			"Thundersong Trumpeter",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Tapping),
            "Disable") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicNoCombatTargetPicker(true,true,false),
				    MagicEvent.NO_DATA,
                    this,
                    "Target creature$ can't attack or block this turn.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.CannotAttackOrBlock));
			}
		}
	};
	
}
