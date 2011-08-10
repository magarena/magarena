package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicManaCost;
import magic.model.event.*;

public class Skithiryx__the_Blight_Dragon {

	public static final MagicPermanentActivation V1791 =new MagicGainActivation(			"Skithiryx, the Blight Dragon",
            MagicManaCost.BLACK,
            MagicAbility.Haste,
            new MagicActivationHints(MagicTiming.FirstMain,false,1)
            );
	
	public static final MagicPermanentActivation V1797 =new MagicRegenerationActivation("Skithiryx, the Blight Dragon",MagicManaCost.BLACK_BLACK);
	
}
