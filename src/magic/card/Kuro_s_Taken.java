package magic.card;

import magic.model.MagicManaCost;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRegenerationActivation;
import magic.model.trigger.MagicBecomesBlockedPumpTrigger;
import magic.model.trigger.MagicWhenBlocksPumpTrigger;

public class Kuro_s_Taken {
	private static final int amount = 1;
	
	public static final MagicBecomesBlockedPumpTrigger T1 = new MagicBecomesBlockedPumpTrigger(amount,amount,false);
	
	public static final MagicWhenBlocksPumpTrigger T2 = new MagicWhenBlocksPumpTrigger(amount,amount);
	
	public static final MagicPermanentActivation A = new MagicRegenerationActivation(MagicManaCost.ONE_BLACK);
}
