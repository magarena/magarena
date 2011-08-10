package magic.card;

import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicNoCombatTargetPicker;

public class Pacifism {

	public static final MagicSpellCardEvent V6514 =new MagicPlayAuraEvent("Pacifism",
			MagicTargetChoice.NEG_TARGET_CREATURE,new MagicNoCombatTargetPicker(true,true,true));
}
