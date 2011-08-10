package magic.card;

import magic.model.*;
import magic.model.trigger.MagicSpecterTrigger;
import magic.model.trigger.MagicTrigger;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;
import magic.model.variable.MagicStaticLocalVariable;

public class Guul_Draz_Specter {
	
    private static final MagicLocalVariable GUUL_DRAZ_SPECTER=new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			if (game.getOpponent(permanent.getController()).getHand().isEmpty()) {
				pt.power+=3;
				pt.toughness+=3;
			}
		}		
	};

    public static final MagicTrigger V7579 =new MagicSpecterTrigger("Guul Draz Specter",true,false);
    
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
		    cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
		    cdef.addLocalVariable(GUUL_DRAZ_SPECTER);
        }
    };
    
}
