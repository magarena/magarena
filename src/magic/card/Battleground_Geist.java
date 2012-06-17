package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.target.MagicTargetFilter;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Battleground_Geist {
    public static final MagicStatic S = new MagicStatic(
    		MagicLayer.ModPT, 
    		MagicTargetFilter.TARGET_SPIRIT_YOU_CONTROL) {
    	@Override
    	public void modPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
    		pt.add(1,0);
    	}
    	@Override
    	public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
    		return source != target;
    	}
    };
}
