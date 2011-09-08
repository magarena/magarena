package magic.card;

import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicManaType;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicTapManaActivation;

public class Birds_of_Paradise {

    public static final MagicManaActivation V1 = new MagicTapManaActivation(MagicManaType.ALL_TYPES, 1);
    
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(final MagicCardDefinition cdef) {
		    cdef.setExcludeManaOrCombat();
        }
    };
}
