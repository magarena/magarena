package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.event.MagicLevelUpActivation;
import magic.model.event.MagicPermanentActivation;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;
import magic.model.variable.MagicStaticLocalVariable;

public class Nirkana_Cutthroat {
	private static final MagicLocalVariable LV = new MagicDummyLocalVariable() {
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

	public static final MagicPermanentActivation A = new MagicLevelUpActivation(MagicManaCost.TWO_BLACK,3);
	
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
		    cdef.addLocalVariable(LV);	
		    cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
		    cdef.setVariablePT();
        }
    };
}
