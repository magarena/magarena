package magic.card;

import magic.model.event.MagicPlayAuraEvent;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicFirstStrikeTargetPicker;

public class Galvanic_Arc {
	public static final MagicPlayAuraEvent S = new MagicPlayAuraEvent(
			MagicTargetChoice.TARGET_CREATURE,
            MagicFirstStrikeTargetPicker.getInstance());
}
