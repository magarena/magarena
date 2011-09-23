package magic.card;

import magic.model.MagicSubType;
import magic.model.trigger.MagicTappedIntoPlayUnlessTrigger;
import magic.model.trigger.MagicTrigger;

public class Clifftop_Retreat {
    public static final MagicTrigger T = new MagicTappedIntoPlayUnlessTrigger(MagicSubType.Mountain,MagicSubType.Plains);
}
