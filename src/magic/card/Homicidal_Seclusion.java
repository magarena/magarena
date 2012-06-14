package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicType;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Homicidal_Seclusion {
    public static final MagicStatic Pt = new MagicStatic(
        MagicLayer.ModPT, 
        MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
        @Override
		public void getPowerToughness(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPowerToughness pt) {
			if (permanent.getController().getNrOfPermanentsWithType(MagicType.Creature) == 1) {
				pt.add(3,1);
			}
        }
    };
    
    public static final MagicStatic Ab = new MagicStatic(
            MagicLayer.Ability, 
            MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
            @Override
		public long getAbilityFlags(
				final MagicGame game,
				final MagicPermanent permanent,
				final long flags) {
    			if (permanent.getController().getNrOfPermanentsWithType(MagicType.Creature) == 1) {
    				return flags | MagicAbility.LifeLink.getMask();
    			}
    			return flags;
            }
        };
}
