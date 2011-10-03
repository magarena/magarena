package magic.card;

import magic.model.MagicManaCost;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRegenerationActivation;

public class Blight_Mamba {
	public static final MagicPermanentActivation A = new MagicRegenerationActivation(MagicManaCost.ONE_GREEN);
}
