package magic.card;

import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicWeakenTargetPicker;

public class Clinging_Darkness {

	public static final MagicSpellCardEvent V6484 =new MagicPlayAuraEvent("Clinging Darkness",
			MagicTargetChoice.NEG_TARGET_CREATURE,new MagicWeakenTargetPicker(-4,-1));
}
