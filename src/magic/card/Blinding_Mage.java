package magic.card;

import magic.model.MagicManaCost;
import magic.model.MagicSource;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTapCreatureActivation;
import magic.model.event.MagicTiming;

public class Blinding_Mage {
	public static final MagicPermanentActivation A = new MagicTapCreatureActivation(
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.WHITE.getCondition()},
			new MagicActivationHints(MagicTiming.Tapping),
			"Tap") {
        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
            return new MagicEvent[]{
                new MagicPayManaCostTapEvent(source,source.getController(),
                MagicManaCost.WHITE)};
        }
    };
}
