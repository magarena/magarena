
package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Naya_Hushblade {
	private static boolean isValid(final MagicPermanent owner, final MagicGame game) {
		for (final MagicPermanent permanent : owner.getController().getPermanents()) {
			if (permanent != owner && MagicColor.isMulti(permanent.getColorFlags(game))) {
				return true;
			}
		}
		return false;
	}
	
	public static final MagicStatic S1 = new MagicStatic(MagicLayer.ModPT) {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			if (isValid(permanent,game)) {
				pt.add(1,1);
			}
		}		
	};
	
	public static final MagicStatic S2 = new MagicStatic(MagicLayer.Ability) {
        @Override
        public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return (isValid(permanent,game)) ?
                flags | MagicAbility.Shroud.getMask() :
                flags;
        }
	}; 
}
