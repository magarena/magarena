package magic.card;

import magic.model.MagicManaType;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicTapManaActivation;

public class Birds_of_Paradise {
    public static final MagicManaActivation V1 = new MagicTapManaActivation(MagicManaType.ALL_TYPES, 1);
}
