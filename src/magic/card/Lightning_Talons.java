package magic.card;

import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicFirstStrikeTargetPicker;

public class Lightning_Talons {

	public static final MagicSpellCardEvent V6510 =new MagicPlayAuraEvent("Lightning Talons",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicFirstStrikeTargetPicker.getInstance());
}
