package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Silver_Inlaid_Dagger {
    public static final MagicStatic S = new MagicStatic(
        MagicLayer.ModPT, 
	    MagicTargetFilter.TARGET_CREATURE) {
        @Override
        public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,0);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {	
        	return source.getEquippedCreature().hasSubType(MagicSubType.Human,game) &&
        			target == source.getEquippedCreature();
        }
    };
}
