package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicManaCost;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicGainActivation;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRegenerationActivation;
import magic.model.event.MagicTiming;

public class Skithiryx__the_Blight_Dragon {
	public static final MagicPermanentActivation A = new MagicGainActivation(
            MagicManaCost.BLACK,
            MagicAbility.Haste,
            new MagicActivationHints(MagicTiming.FirstMain,false,1),
            "Haste");
	
    public static final MagicPermanentActivation A2 = new MagicRegenerationActivation(MagicManaCost.BLACK_BLACK);
}
