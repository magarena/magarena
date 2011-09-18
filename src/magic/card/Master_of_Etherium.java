package magic.card;

import magic.model.MagicGame;
import magic.model.mstatic.MagicLayer;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.variable.MagicDummyLocalVariable;
import java.util.Collection;

public class Master_of_Etherium {
    //Characteristic defining ability
	public static final MagicDummyLocalVariable LV = new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
			final Collection<MagicTarget> targets =
					game.filterTargets(
					permanent.getController(),
					MagicTargetFilter.TARGET_ARTIFACT_YOU_CONTROL);
			pt.add(targets.size(), targets.size());
		}
    };
    
    public static final MagicStatic S = new MagicStatic(
    		MagicLayer.ModPT, 
    		MagicTargetFilter.TARGET_ARTIFACT_CREATURE_YOU_CONTROL) {
    	@Override
    	public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
    		pt.add(1,1);
    	}
    	@Override
    	public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
    		return source != target;
    	}
    };
}
