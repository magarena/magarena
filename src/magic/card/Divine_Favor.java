package magic.card;

import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.target.MagicPumpTargetPicker;

public class Divine_Favor {
	public static final MagicPlayAuraEvent A = new MagicPlayAuraEvent(
			MagicTargetChoice.POS_TARGET_CREATURE,
			MagicPumpTargetPicker.getInstance());
}
