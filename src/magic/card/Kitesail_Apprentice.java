
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

public class Kitesail_Apprentice {
	
	private static final MagicLocalVariable KITESAIL_APPRENTICE=new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			if (permanent.isEquipped()) {
				pt.power++;
				pt.toughness++;
			}
		}
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return permanent.isEquipped()?flags|MagicAbility.Flying.getMask():flags;
		}
	};

    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
            cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
            cdef.addLocalVariable(KITESAIL_APPRENTICE);
        }
    };
}
