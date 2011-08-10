package magic.card;

import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicHasteTargetPicker;

public class Goblin_War_Paint {

	public static final MagicSpellCardEvent V6504 =new MagicPlayAuraEvent("Goblin War Paint",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicHasteTargetPicker.getInstance());
}
