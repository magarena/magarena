package magic.card;

import magic.model.*;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;
import magic.model.variable.MagicStaticLocalVariable;

public class Goblin_Gaveleer {
	
    private static final MagicLocalVariable GOBLIN_GAVELEER=new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			if (permanent.isEquipped()) {
				pt.power+=permanent.getEquipmentPermanents().size()<<1;
			}
		}		
	};

    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
		    cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
		    cdef.addLocalVariable(GOBLIN_GAVELEER);
        }
    };
}
