package magic.card;

import magic.model.MagicManaCost;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPumpActivation;
import magic.model.event.MagicRegenerationActivation;

public class Hellkite_Overlord {

	public static final MagicPermanentActivation V1139 =new MagicPumpActivation("Hellkite Overlord",MagicManaCost.RED,1,0);

	public static final MagicPermanentActivation V1141 =new MagicRegenerationActivation("Hellkite Overlord",MagicManaCost.BLACK_GREEN);
	
}
