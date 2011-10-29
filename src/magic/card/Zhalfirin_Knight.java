package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicManaCost;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicGainActivation;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.trigger.MagicFlankingTrigger;

public class Zhalfirin_Knight {
    public static final MagicFlankingTrigger T = new MagicFlankingTrigger();
    
    public static final MagicPermanentActivation A = new MagicGainActivation(
            MagicManaCost.WHITE_WHITE,
            MagicAbility.FirstStrike,
            new MagicActivationHints(MagicTiming.Pump,false,1),
            "First strike");
}
