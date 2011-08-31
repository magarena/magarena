package magic.card;

import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.trigger.MagicSpecterTrigger;
import magic.model.trigger.MagicTrigger;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;
import magic.model.variable.MagicStaticLocalVariable;

public class Guul_Draz_Specter {
    private static final MagicLocalVariable LV = new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			if (game.getOpponent(permanent.getController()).getHand().isEmpty()) {
				pt.power+=3;
				pt.toughness+=3;
			}
		}		
	};

    public static final MagicTrigger T = new MagicSpecterTrigger(true,false);
    
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
		    cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
		    cdef.addLocalVariable(LV);
        }
    };
}
