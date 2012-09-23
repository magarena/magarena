package magic.card;

import magic.model.MagicPermanent;
import magic.model.MagicAbility;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Shifting_Sliver {
	
	public static final MagicStatic S = new MagicStatic(
	        MagicLayer.Ability, 
	        MagicTargetFilter.TARGET_SLIVER) {
	        @Override
	        public long getAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final long flags) {
	            return flags | MagicAbility.CannotBeBlockedExceptBySliver.getMask();
	        }
	};
}
	
	