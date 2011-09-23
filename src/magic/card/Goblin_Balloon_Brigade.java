package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicManaCost;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicGainActivation;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;

public class Goblin_Balloon_Brigade {
	public static final MagicPermanentActivation A = new MagicGainActivation(
            MagicManaCost.RED,
            MagicAbility.Flying,
            new MagicActivationHints(MagicTiming.Pump,true,1),
            "Flying");
}
