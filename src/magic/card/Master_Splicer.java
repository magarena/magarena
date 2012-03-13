package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Master_Splicer {
	public static final MagicStatic S = new MagicStatic(
			MagicLayer.ModPT, 
			MagicTargetFilter.TARGET_GOLEM_YOU_CONTROL) {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.add(1,1);
		}
	};
	    
    public static final Object T = Blade_Splicer.T;
}
