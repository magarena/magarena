package magic.card;

import magic.model.trigger.MagicDieDrawCardTrigger;
import magic.model.trigger.MagicTrigger;

public class Surveilling_Sprite {
    //always draw card, although rules text says may draw 
    public static final MagicTrigger T1 = MagicDieDrawCardTrigger.INSTANCE;
}
