package magic.card;

import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicManaType;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicTapManaActivation;

public class Alloy_Myr {

    public static final MagicManaActivation V1 = new MagicTapManaActivation(MagicManaType.ALL_TYPES, 1);
    
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
		    cdef.setExcludeManaOrCombat();
        }
    };
}
