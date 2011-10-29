package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicManaCost;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicGainActivation;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;

public class Mantis_Engine {
	// gains flying until end of turn
	public static final MagicPermanentActivation A1 = new MagicGainActivation(
            MagicManaCost.TWO,
            MagicAbility.Flying,
            new MagicActivationHints(MagicTiming.Pump,false,1),
            "Flying");
	
	// gains first strike until end of turn
	public static final MagicPermanentActivation A2 = new MagicGainActivation(
            MagicManaCost.TWO,
            MagicAbility.FirstStrike,
            new MagicActivationHints(MagicTiming.Pump,false,1),
            "First strike");
}
