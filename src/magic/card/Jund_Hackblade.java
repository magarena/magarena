package magic.card;

import java.util.Arrays;

import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.variable.*;
import magic.model.*;

public class Jund_Hackblade {
	private static final MagicLocalVariable JUND_HACKBLADE=new MagicBladeLocalVariable(MagicAbility.Haste.getMask()); 
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
            cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
            cdef.addLocalVariable(JUND_HACKBLADE);		
        }
    };
}
