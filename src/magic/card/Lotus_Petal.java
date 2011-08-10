package magic.card;

import magic.model.MagicManaType;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicSacrificeTapManaActivation;

public class Lotus_Petal {
		public static final MagicManaActivation V1 = new MagicSacrificeTapManaActivation(MagicManaType.ALL_TYPES);
}
