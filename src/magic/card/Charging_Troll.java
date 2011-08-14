package magic.card;

import magic.model.MagicManaCost;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRegenerationActivation;

public class Charging_Troll {
	public static final MagicPermanentActivation A = new MagicRegenerationActivation(MagicManaCost.GREEN);
}
