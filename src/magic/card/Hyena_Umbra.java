package magic.card;

import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicFirstStrikeTargetPicker;

public class Hyena_Umbra {

	public static final MagicSpellCardEvent V6508 =new MagicPlayAuraEvent("Hyena Umbra",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicFirstStrikeTargetPicker.getInstance());
}
