package magic.card;

import magic.model.*;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicTapTargetPicker;

public class Gideon_s_Lawkeeper {
	public static final MagicPermanentActivation A1 = new MagicTapCreatureActivation(
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
