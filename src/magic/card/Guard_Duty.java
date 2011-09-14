package magic.card;

import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicNoCombatTargetPicker;

public class Guard_Duty {
	public static final MagicSpellCardEvent S =new MagicPlayAuraEvent(
			MagicTargetChoice.NEG_TARGET_CREATURE,
            new MagicNoCombatTargetPicker(true,false,true));
}
