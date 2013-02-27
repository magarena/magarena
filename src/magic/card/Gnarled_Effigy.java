package magic.card;

import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.event.MagicWeakenCreatureActivation;

public class Gnarled_Effigy {
    public static final MagicPermanentActivation A = new MagicWeakenCreatureActivation(
            new MagicCondition[]{
                MagicCondition.CAN_TAP_CONDITION,
                MagicConditionFactory.ManaCost("{4}")
            },
            new MagicActivationHints(MagicTiming.Removal),
            "-1/-1") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{new MagicPayManaCostTapEvent(
                source,
                source.getController(),
                MagicManaCost.create("{4}")
            )};
        }
    };
}
