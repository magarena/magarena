package magic.card;

import magic.model.MagicSubType;
import magic.model.trigger.MagicTappedIntoPlayUnlessTrigger;
import magic.model.trigger.MagicTrigger;

public class Isolated_Chapel {
    public static final MagicTrigger T = new MagicTappedIntoPlayUnlessTrigger(MagicSubType.Plains,MagicSubType.Swamp);
}
