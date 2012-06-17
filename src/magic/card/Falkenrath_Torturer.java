package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetNone;

public class Falkenrath_Torturer {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{
                MagicCondition.ONE_CREATURE_CONDITION
            },
            new MagicActivationHints(MagicTiming.Pump),
            "Pump") {
        
        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{new MagicSacrificePermanentEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.SACRIFICE_CREATURE)};
        }
        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            String message = source + " gains flying until end of turn.";
            boolean isHuman = false;
            final MagicTarget target = payedCost.getTarget();
            if (target != MagicTargetNone.getInstance()) {
                final MagicPermanent sacrificed = (MagicPermanent)payedCost.getTarget();
                isHuman = sacrificed.getCardDefinition().hasSubType(MagicSubType.Human);
                if (isHuman) {
                    message += " Put a +1/+1 counter on " + source + ".";
                }
            }
            return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source,isHuman},
                    this,
                    message);
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            game.doAction(new MagicSetAbilityAction(
                    (MagicPermanent)data[0],
                    MagicAbility.Flying));
            if ((Boolean)data[1]) {
                game.doAction(new MagicChangeCountersAction(
                        (MagicPermanent)data[0],
                        MagicCounterType.PlusOne,
                        1,
                        true));
            }
        }
    };
}
