package magic.card;

import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicFlyingTargetPicker;

public class Flight {
	public static final MagicSpellCardEvent S = new MagicPlayAuraEvent(
			MagicTargetChoice.POS_TARGET_CREATURE,
            MagicFlyingTargetPicker.getInstance());
}
