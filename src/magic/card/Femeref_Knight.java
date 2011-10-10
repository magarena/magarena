package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicManaCost;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicGainActivation;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.trigger.MagicFlankingTrigger;

public class Femeref_Knight {
    public static final MagicFlankingTrigger T = new MagicFlankingTrigger();
    
    public static final MagicPermanentActivation A = new MagicGainActivation(
            MagicManaCost.WHITE,
            MagicAbility.Vigilance,
            new MagicActivationHints(MagicTiming.Pump,true,1),
            "Vigilance");
}
