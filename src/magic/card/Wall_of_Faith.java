package magic.card;

import magic.model.MagicManaCost;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPumpActivation;

public class Wall_of_Faith {
	public static final MagicPermanentActivation A = new MagicPumpActivation(MagicManaCost.WHITE,0,1);
}
