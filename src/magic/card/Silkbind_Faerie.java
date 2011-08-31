package magic.card;

import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapCreatureActivation;
import magic.model.event.MagicTiming;
import magic.model.event.MagicUntapEvent;

public class Silkbind_Faerie {
	public static final MagicPermanentActivation A1 = new MagicTapCreatureActivation(
			new MagicCondition[]{MagicCondition.CAN_UNTAP_CONDITION,MagicManaCost.ONE_WHITE_OR_BLUE.getCondition()},
			new MagicActivationHints(MagicTiming.Tapping),
            "Tap") {
        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_WHITE_OR_BLUE),
				new MagicUntapEvent((MagicPermanent)source)};
        }
    };
}
