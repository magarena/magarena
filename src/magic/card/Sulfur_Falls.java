package magic.card;

import magic.model.MagicSubType;
import magic.model.trigger.MagicTappedIntoPlayUnlessTrigger;
import magic.model.trigger.MagicTrigger;

public class Sulfur_Falls {
    public static final MagicTrigger T = new MagicTappedIntoPlayUnlessTrigger(MagicSubType.Island,MagicSubType.Mountain);
}
