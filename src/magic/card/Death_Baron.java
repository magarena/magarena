package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.MagicSubType;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Death_Baron {
	public static final MagicStatic S = new MagicStatic(
			MagicLayer.ModPT, 
			MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.add(1,1);
		}
		@Override
		public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
			return target.hasSubType(MagicSubType.Skeleton,game) || 
					(source != target && target.hasSubType(MagicSubType.Zombie,game));
		}
	};
    
    public static final MagicStatic S2 = new MagicStatic(
    		MagicLayer.Ability, 
    		MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
    	@Override
    	public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
    		return flags | MagicAbility.Deathtouch.getMask();
    	}
    	@Override
    	public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
    		return target.hasSubType(MagicSubType.Skeleton,game) || 
    				(source != target && target.hasSubType(MagicSubType.Zombie,game));
    	}
    };
}
