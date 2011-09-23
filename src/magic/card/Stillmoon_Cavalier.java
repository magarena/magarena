package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicManaCost;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicGainActivation;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPumpActivation;
import magic.model.event.MagicTiming;

public class Stillmoon_Cavalier {
	// gains flying until end of turn
	public static final MagicPermanentActivation A = new MagicGainActivation(
            MagicManaCost.WHITE_OR_BLACK,
            MagicAbility.Flying,
            new MagicActivationHints(MagicTiming.Pump,true,1),
            "Flying");
	
	// gains first strike until end of turn
	public static final MagicPermanentActivation A2 = new MagicGainActivation(
            MagicManaCost.WHITE_OR_BLACK,
            MagicAbility.FirstStrike,
            new MagicActivationHints(MagicTiming.Pump,true,1),
            "First strike");
	
	// gets +1/+0 until end of turn
	public static final MagicPermanentActivation A3 = new MagicPumpActivation(MagicManaCost.WHITE_OR_BLACK_WHITE_OR_BLACK,1,0);
}
