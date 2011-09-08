package magic.card;

import magic.model.trigger.MagicSpecterTrigger;
import magic.model.trigger.MagicTrigger;

public class Hypnotic_Specter {
    //should not cause discard if player is not opponent
    public static final MagicTrigger T = new MagicSpecterTrigger(false,true);
}
