package magic.card;

import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicUnblockableTargetPicker;

public class Protective_Bubble {

	public static final MagicSpellCardEvent V6518 =new MagicPlayAuraEvent("Protective Bubble",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicUnblockableTargetPicker.getInstance());
}
