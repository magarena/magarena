package magic.card;

import magic.model.*;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicMustAttackTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.action.MagicPermanentAction;

public class Alluring_Siren {
	public static final MagicPermanentActivation A = new MagicPermanentActivation( 
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.MustAttack),
            "Attacks") {
		
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                    MagicMustAttackTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Target creature$ an opponent controls attacks you this turn if able.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.AttacksEachTurnIfAble));
                }
			});
		}
	};
}
