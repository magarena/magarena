package magic.card;

import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicWeakenTargetPicker;

public class Dead_Weight {
	public static final MagicSpellCardEvent S = new MagicPlayAuraEvent(
			MagicTargetChoice.NEG_TARGET_CREATURE,
            new MagicWeakenTargetPicker(-2,-2));
}
