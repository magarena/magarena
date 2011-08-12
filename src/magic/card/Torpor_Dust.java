package magic.card;

import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicWeakenTargetPicker;

public class Torpor_Dust {
	public static final MagicSpellCardEvent E = new MagicPlayAuraEvent(
			MagicTargetChoice.NEG_TARGET_CREATURE,new MagicWeakenTargetPicker(3,0));
}
