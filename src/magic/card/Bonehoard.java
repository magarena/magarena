package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Bonehoard {
    public static final MagicStatic S = new MagicStatic(
        MagicLayer.ModPT, 
	    MagicTargetFilter.TARGET_CREATURE) {
    	
    	private int amount = 0;
    	
        @Override
        public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(amount,amount);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
        	if (target == source.getEquippedCreature()) {
        		amount = game.filterTargets(
						game.getPlayer(0),
						MagicTargetFilter.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS).size();
        		return true;
        	}
        	return false;
        }
    };
}