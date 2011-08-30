
package magic.card;

import magic.model.*;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;
import magic.model.variable.MagicStaticLocalVariable;

public class Thran_Golem {
	
	private static final MagicLocalVariable LV = new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			if (permanent.isEnchanted()) {
				pt.power += 2;
				pt.toughness += 2;
			}
		}
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return permanent.isEnchanted() ? 
					flags|MagicAbility.Flying.getMask() |
						MagicAbility.FirstStrike.getMask() |
						MagicAbility.Trample.getMask() :
					flags;
		}
	};

    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
            cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
            cdef.addLocalVariable(LV);
        }
    };
}
