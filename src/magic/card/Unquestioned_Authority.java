package magic.card;

import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.target.MagicUnblockableTargetPicker;

public class Unquestioned_Authority {
	public static final MagicPlayAuraEvent S = new MagicPlayAuraEvent(
            MagicTargetChoice.POS_TARGET_CREATURE,
            MagicUnblockableTargetPicker.getInstance());
}
