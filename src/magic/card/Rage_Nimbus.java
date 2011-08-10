package magic.card;

import magic.model.*;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicMustAttackTargetPicker;
import magic.model.target.MagicTarget;

public class Rage_Nimbus {

	public static final MagicPermanentActivation V1538 =new MagicPermanentActivation(            "Rage Nimbus",
			new MagicCondition[]{MagicManaCost.ONE_RED.getCondition()},
            new MagicActivationHints(MagicTiming.MustAttack),
            "Attacks"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_RED)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(source,source.getController(),MagicTargetChoice.NEG_TARGET_CREATURE,MagicMustAttackTargetPicker.getInstance(),
				MagicEvent.NO_DATA,this,"Target creature$ attacks this turn if able.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				game.doAction(new MagicSetAbilityAction((MagicPermanent)target,MagicAbility.AttacksEachTurnIfAble));
			}
		}
	};

}
