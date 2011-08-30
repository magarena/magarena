package magic.card;

import magic.model.*;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicMustAttackTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.action.MagicPermanentAction;

public class Rage_Nimbus {
	public static final MagicPermanentActivation A = new MagicPermanentActivation( 
			new MagicCondition[]{MagicManaCost.ONE_RED.getCondition()},
            new MagicActivationHints(MagicTiming.MustAttack),
            "Attacks") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_RED)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    MagicMustAttackTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Target creature$ attacks this turn if able.");
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
