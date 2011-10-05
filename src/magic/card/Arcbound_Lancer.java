package magic.card;

import magic.model.MagicCounterType;
import magic.model.trigger.MagicComesIntoPlayWithCounterTrigger;
import magic.model.trigger.MagicModularTrigger;

public class Arcbound_Lancer {
	public static final MagicComesIntoPlayWithCounterTrigger T1 = 
			new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.PlusOne,"+1/+1",4);
    
    public static final MagicModularTrigger T2 = new MagicModularTrigger();
}
