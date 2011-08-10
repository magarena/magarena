package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.variable.MagicBladeLocalVariable;
import magic.model.variable.MagicLocalVariable;
import magic.model.variable.MagicStaticLocalVariable;

public class Esper_Stormblade {
	private static final MagicLocalVariable ESPER_STORMBLADE=new MagicBladeLocalVariable(MagicAbility.Flying.getMask());
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
            cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
            cdef.addLocalVariable(ESPER_STORMBLADE);		
        }
    };
}
