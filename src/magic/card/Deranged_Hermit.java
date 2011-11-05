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
import magic.model.trigger.MagicEchoTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Deranged_Hermit {
	public static final MagicStatic S = new MagicStatic(
	        MagicLayer.ModPT, 
	        MagicTargetFilter.TARGET_SQUIRREL_CREATURE) {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.add(1,1);
		}
	};
	    
	public static final MagicEchoTrigger T1 = new MagicEchoTrigger();
	
	public static final MagicWhenComesIntoPlayTrigger T2 = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
			return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player},
                    this,
                    player + " puts four 1/1 green Squirrel creature tokens onto the battlefield.");
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			for (int i=4;i>0;i--) {
				game.doAction(new MagicPlayTokenAction(
						(MagicPlayer)data[0],
						TokenCardDefinitions.getInstance().getTokenDefinition("Squirrel1")));
			}
		}		
    };
}
