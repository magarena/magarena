package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicMustAttackTargetPicker;

public class Alluring_Siren {
    public static final MagicPermanentActivation A = new MagicPermanentActivation( 
        new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
        new MagicActivationHints(MagicTiming.MustAttack),
        "Attacks"
    ) {
        
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{new MagicTapEvent(source)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS,
                MagicMustAttackTargetPicker.create(),
                this,
                "Target creature$ an opponent controls attacks you this turn if able."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.AttacksEachTurnIfAble));
                }
            });
        }
    };
}
