package magic.card;

import magic.model.MagicManaCost;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPumpActivation;
import magic.model.trigger.MagicEchoTrigger;

public class Lightning_Dragon {
	public static final MagicPermanentActivation A = new MagicPumpActivation(MagicManaCost.RED,1,0);
}
