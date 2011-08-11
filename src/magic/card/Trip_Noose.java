package magic.card;

import magic.model.*;
import magic.model.condition.MagicCondition;
import magic.model.event.*;

public class Trip_Noose {
    public static final MagicPermanentActivation A1 = new MagicTapCreatureActivation(
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.TWO.getCondition()},
            new MagicActivationHints(MagicTiming.Tapping),
            "Tap") {
        @Override
        public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
                new MagicPayManaCostTapEvent(source,source.getController(),
                MagicManaCost.TWO)};
        }
    };
}
