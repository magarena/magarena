package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.variable.MagicBladeLocalVariable;
import magic.model.variable.MagicLocalVariable;
import magic.model.variable.MagicStaticLocalVariable;

public class Grixis_Grimblade {
	private static final MagicLocalVariable GRIXIS_GRIMBLADE=new MagicBladeLocalVariable(MagicAbility.Deathtouch.getMask());
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
            cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
            cdef.addLocalVariable(GRIXIS_GRIMBLADE);		
        }
    };
}
