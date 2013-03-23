package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicUntapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapCreatureActivation;
import magic.model.event.MagicTiming;
import magic.model.target.MagicTapTargetPicker;

public class Niblis_of_the_Breath {
    public static final MagicPermanentActivation A1 = new MagicTapCreatureActivation(
            new MagicCondition[]{
                    MagicCondition.CAN_TAP_CONDITION,
                    MagicConditionFactory.ManaCost("{U}")
            },
            new MagicActivationHints(MagicTiming.Tapping),
            "Tap") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                new MagicPayManaCostTapEvent(
                        source,
                        source.getController(),
                        MagicManaCost.create("{U}"))
            };
        }
    };
    
    public static final MagicPermanentActivation A2 = new MagicPermanentActivation(
            new MagicCondition[]{
                    MagicCondition.CAN_TAP_CONDITION,
                    MagicConditionFactory.ManaCost("{U}")
            },
            new MagicActivationHints(MagicTiming.Tapping),
            "Untap") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                new MagicPayManaCostTapEvent(
                        source,
                        source.getController(),
                        MagicManaCost.create("{U}"))
            };
        }

        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_CREATURE,
                new MagicTapTargetPicker(false,true),
                this,
                "Untap target creature$."
            );
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicUntapAction(creature));
                }
            });
        }
    };
}
