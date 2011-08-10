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

public class Student_of_Warfare {
	
    private static final MagicLocalVariable STUDENT_OF_WARFARE=new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			final int charges=permanent.getCounters(MagicCounterType.Charge);
			if (charges>=7) {
				pt.power=4;
				pt.toughness=4;
			} else if (charges>=2) {
				pt.power=3;
				pt.toughness=3;
			}
		}		
		
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			final int charges=permanent.getCounters(MagicCounterType.Charge);
			if (charges>=7) {
				return flags|MagicAbility.DoubleStrike.getMask();
			} else if (charges>=2) {
				return flags|MagicAbility.FirstStrike.getMask();
			}
			return flags;
		}
	};

	public static final MagicPermanentActivation V1891 = new MagicLevelUpActivation("Student of Warfare",MagicManaCost.WHITE,7);
		
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
		    cdef.addLocalVariable(STUDENT_OF_WARFARE);	
		    cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
		    cdef.setVariablePT();
        }
    };
}
