package magic.card;

import magic.model.*;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicTapTargetPicker;

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
