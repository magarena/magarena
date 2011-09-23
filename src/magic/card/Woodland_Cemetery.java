package magic.card;

import magic.model.MagicSubType;
import magic.model.trigger.MagicTappedIntoPlayUnlessTrigger;
import magic.model.trigger.MagicTrigger;

public class Woodland_Cemetery {
    public static final MagicTrigger T = new MagicTappedIntoPlayUnlessTrigger(MagicSubType.Swamp,MagicSubType.Forest);
}
