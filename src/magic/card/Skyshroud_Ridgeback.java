package magic.card;

import magic.model.MagicCounterType;
import magic.model.trigger.MagicComesIntoPlayWithCounterTrigger;
import magic.model.trigger.MagicFadeVanishCounterTrigger;

public class Skyshroud_Ridgeback {
	public static final MagicComesIntoPlayWithCounterTrigger T1 = 
			new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.Charge,"fade",2);
	
    public static final MagicFadeVanishCounterTrigger T2 = new MagicFadeVanishCounterTrigger("fade");
}
