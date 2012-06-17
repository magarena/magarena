package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Serra_Ascendant {
	public static final MagicStatic S1 = new MagicStatic(MagicLayer.ModPT) {
		@Override
		public void modPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			if (permanent.getController().getLife() >= 30) {
				pt.add(5,5);
			}
		}
	};
	
	public static final MagicStatic S2 = new MagicStatic(MagicLayer.Ability) {
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return permanent.getController().getLife() >= 30 ?
				flags|MagicAbility.Flying.getMask() : flags;
		}
	};
}
