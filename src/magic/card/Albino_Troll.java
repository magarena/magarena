package magic.card;

import magic.model.MagicManaCost;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRegenerationActivation;
import magic.model.trigger.MagicEchoTrigger;

public class Albino_Troll {
	public static final MagicPermanentActivation A =
			new MagicRegenerationActivation(MagicManaCost.ONE_GREEN);
}
