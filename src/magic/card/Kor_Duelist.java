
package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.mstatic.MagicLayer;
import magic.model.MagicPermanent;
import magic.model.mstatic.MagicStatic;

public class Kor_Duelist {
	public static final MagicStatic S = new MagicStatic(MagicLayer.Ability) {
		@Override
		public long getAbilityFlags(
                final MagicGame game,
                final MagicPermanent permanent,
                final long flags) {
			return permanent.isEquipped() ? 
				flags|MagicAbility.DoubleStrike.getMask():
	            flags;
		}
    };
}
