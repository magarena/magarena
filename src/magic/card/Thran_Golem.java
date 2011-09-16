
package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;

public class Thran_Golem {
	public static final MagicLocalVariable LV = new MagicDummyLocalVariable() {
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
}
