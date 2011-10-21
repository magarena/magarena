package magic.card;

import magic.model.MagicManaCost;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPumpActivation;

public class AEtherflame_Wall {
	public static final MagicPermanentActivation A = new MagicPumpActivation(MagicManaCost.RED,1,0);
}
