package magic.card;

import magic.model.MagicManaCost;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRegenerationActivation;
import magic.model.trigger.MagicSpecterTrigger;
import magic.model.trigger.MagicTrigger;

public class Chilling_Apparition {
    public static final MagicTrigger T = new MagicSpecterTrigger(true,false,false);
    
    public static final MagicPermanentActivation A = new MagicRegenerationActivation(MagicManaCost.BLACK);
}
