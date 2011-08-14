package magic.card;

import magic.model.MagicManaCost;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPumpActivation;
import magic.model.event.MagicRegenerationActivation;

public class Hellkite_Overlord {
	public static final MagicPermanentActivation A1 = new MagicPumpActivation(MagicManaCost.RED,1,0);
	public static final MagicPermanentActivation A2 = new MagicRegenerationActivation(MagicManaCost.BLACK_GREEN);
}
