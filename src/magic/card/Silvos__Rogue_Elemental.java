package magic.card;

import magic.model.MagicManaCost;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRegenerationActivation;

public class Silvos__Rogue_Elemental {
	public static final MagicPermanentActivation R = new MagicRegenerationActivation(
            MagicManaCost.GREEN);
}
