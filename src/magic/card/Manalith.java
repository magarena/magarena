package magic.card;

import magic.model.MagicManaType;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicTapManaActivation;

public class Manalith {
	public static final MagicManaActivation M = new MagicTapManaActivation(MagicManaType.ALL_TYPES, 2);
}
