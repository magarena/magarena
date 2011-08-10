
package magic.card;

import magic.model.*;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;
import magic.model.variable.MagicStaticLocalVariable;

public class Ruthless_Cullblade {
	
	private static final MagicLocalVariable RUTHLESS_CULLBLADE=new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			if (game.getOpponent(permanent.getController()).getLife()<=10) {
				pt.power+=2;
				pt.toughness++;
			}
		}		
	};

    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
            cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
            cdef.addLocalVariable(RUTHLESS_CULLBLADE);	
        }
    };
}
