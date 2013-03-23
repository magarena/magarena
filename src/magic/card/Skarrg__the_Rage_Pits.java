package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.target.MagicPumpTargetPicker;

public class Skarrg__the_Rage_Pits {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{
                MagicCondition.CAN_TAP_CONDITION,
                MagicConditionFactory.ManaCost("{R}{G}")
            },
            new MagicActivationHints(MagicTiming.Pump),
            "Pump") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.create("{R}{G}"))
            };
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature$ gets +1/+1 and gains trample until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,1,1));
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Trample));
                }
            });
        }
    };
}
