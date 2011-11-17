package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRemoveCounterEvent;
import magic.model.event.MagicTapCreatureActivation;
import magic.model.event.MagicTiming;

public class Jolting_Merfolk {
    public static final MagicPermanentActivation A = new MagicTapCreatureActivation(
			new MagicCondition[]{MagicCondition.CHARGE_COUNTER_CONDITION},
            new MagicActivationHints(MagicTiming.Tapping),
            "Tap") {
        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicRemoveCounterEvent((MagicPermanent)source,MagicCounterType.Charge,1)};
        }
    };
}
