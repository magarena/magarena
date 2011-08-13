package magic.card;

import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicFirstStrikeTargetPicker;

public class Lightning_Talons {
	public static final MagicSpellCardEvent S =new MagicPlayAuraEvent(
			MagicTargetChoice.POS_TARGET_CREATURE,
            MagicFirstStrikeTargetPicker.getInstance());
}
