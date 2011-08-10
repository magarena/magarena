package magic.card;

import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicPumpTargetPicker;

public class Epic_Proportions {

	public static final MagicSpellCardEvent V6494 =new MagicPlayAuraEvent("Epic Proportions",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.getInstance());
}
