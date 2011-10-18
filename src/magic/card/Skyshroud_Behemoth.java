package magic.card;

import magic.model.MagicCounterType;
import magic.model.trigger.MagicComesIntoPlayWithCounterTrigger;
import magic.model.trigger.MagicFadeVanishCounterTrigger;
import magic.model.trigger.MagicTappedIntoPlayTrigger;
import magic.model.trigger.MagicTrigger;

public class Skyshroud_Behemoth {
	public static final MagicComesIntoPlayWithCounterTrigger T1 = 
			new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.Charge,"fade",2);
	
    public static final MagicFadeVanishCounterTrigger T2 = new MagicFadeVanishCounterTrigger("fade");
    
    public static final MagicTrigger T3 = new MagicTappedIntoPlayTrigger();
}
