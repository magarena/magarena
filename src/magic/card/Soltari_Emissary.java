package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicManaCost;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicGainActivation;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;

public class Soltari_Emissary {
	public static final MagicPermanentActivation A = new MagicGainActivation(
            MagicManaCost.WHITE,
            MagicAbility.Shadow,
            new MagicActivationHints(MagicTiming.Pump,false,1),
            "Shadow");
}
