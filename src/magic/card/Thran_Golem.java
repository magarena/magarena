
package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Thran_Golem {
	public static final MagicStatic S1 = new MagicStatic(MagicLayer.ModPT) {
		@Override
		public void modPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			if (permanent.isEnchanted()) {
				pt.add(2,2);
			}
		}	
	};
	
	public static final MagicStatic S2 = new MagicStatic(MagicLayer.Ability) {
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return permanent.isEnchanted() ? 
					flags |
                        MagicAbility.Flying.getMask() |
						MagicAbility.FirstStrike.getMask() |
						MagicAbility.Trample.getMask() :
					flags;
		}
	};
}
