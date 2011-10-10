package magic.card;

import magic.model.MagicManaCost;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRegenerationActivation;
import magic.model.trigger.MagicFlankingTrigger;

public class Cadaverous_Knight {
    public static final MagicFlankingTrigger T = new MagicFlankingTrigger();
    
    public static final MagicPermanentActivation A = new MagicRegenerationActivation(MagicManaCost.ONE_BLACK_BLACK);
}
