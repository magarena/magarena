package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicManaCost;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicGainActivation;
import magic.model.event.MagicTiming;

public class Stillmoon_Cavalier {
	// gains flying until end of turn
	public static final MagicGainActivation A = new MagicGainActivation(
            MagicManaCost.WHITE_OR_BLACK,
            MagicAbility.Flying,
            new MagicActivationHints(MagicTiming.Pump,false,1),
            "Flying");
	
	// gains first strike until end of turn
	public static final MagicGainActivation A2 = new MagicGainActivation(
            MagicManaCost.WHITE_OR_BLACK,
            MagicAbility.FirstStrike,
            new MagicActivationHints(MagicTiming.Pump,false,1),
            "First strike");
}
