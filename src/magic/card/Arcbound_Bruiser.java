package magic.card;

import magic.model.MagicCounterType;
import magic.model.trigger.MagicComesIntoPlayWithCounter;
import magic.model.trigger.MagicModularTrigger;

public class Arcbound_Bruiser {
	public static final MagicComesIntoPlayWithCounter T1 = new MagicComesIntoPlayWithCounter(MagicCounterType.PlusOne,3);
    
    public static final MagicModularTrigger T2 = new MagicModularTrigger();
}
