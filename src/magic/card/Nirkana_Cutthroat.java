package magic.card;
import java.util.*;
import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;
import magic.data.*;
import magic.model.variable.*;

public class Nirkana_Cutthroat {

	private static final MagicLocalVariable NIRKANA_CUTTHROAT=new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			final int charges=permanent.getCounters(MagicCounterType.Charge);
			if (charges>=3) {
				pt.power=5;
				pt.toughness=4;
			} else if (charges>=1) {
				pt.power=4;
				pt.toughness=3;
			}
		}
		
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			final int charges=permanent.getCounters(MagicCounterType.Charge);
			if (charges>=3) {
				return flags|MagicAbility.FirstStrike.getMask()|MagicAbility.Deathtouch.getMask();
			} else if (charges>=1) {
				return flags|MagicAbility.Deathtouch.getMask();
			}
			return flags;
		}
	};

	public static final MagicPermanentActivation V1413 = new MagicLevelUpActivation("Nirkana Cutthroat",MagicManaCost.TWO_BLACK,3);
	
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
		    cdef.addLocalVariable(NIRKANA_CUTTHROAT);	
		    cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
		    cdef.setVariablePT();
        }
    };
}
