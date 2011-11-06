package magic.card;

import magic.model.MagicManaCost;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPumpActivation;
import magic.model.trigger.MagicEchoTrigger;

public class Henchfiend_of_Ukor {
	public static final MagicEchoTrigger T = new MagicEchoTrigger("{1}{B}");
	
	public static final MagicPermanentActivation A = new MagicPumpActivation(MagicManaCost.BLACK_OR_RED,1,0);
}
