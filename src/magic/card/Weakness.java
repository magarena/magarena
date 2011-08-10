package magic.card;

import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicWeakenTargetPicker;

public class Weakness {

	public static final MagicSpellCardEvent V6536 =new MagicPlayAuraEvent("Weakness",
			MagicTargetChoice.NEG_TARGET_CREATURE,new MagicWeakenTargetPicker(2,1));
	
}
