package magic.card;

import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicPumpTargetPicker;

public class Drake_Umbra {

	public static final MagicSpellCardEvent V6486 =new MagicPlayAuraEvent("Drake Umbra",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance());
}
