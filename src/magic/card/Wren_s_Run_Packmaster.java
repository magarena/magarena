package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicPlayTokenAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Wren_s_Run_Packmaster {
	public static final MagicStatic S = new MagicStatic(
			MagicLayer.Ability, 
			MagicTargetFilter.TARGET_WOLF_YOU_CONTROL) {
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return flags | MagicAbility.Deathtouch.getMask();
		}
	};
	    
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicManaCost.TWO_GREEN.getCondition()},
            new MagicActivationHints(MagicTiming.Token,true),
            "Token"
            ) {
		
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_GREEN)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player = source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player},
                    this,
                    player + " puts a 2/2 green Wolf creature token onto the battlefield.");
        }
		
        @Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
        	game.doAction(new MagicPlayTokenAction(
        			(MagicPlayer)data[0],
        			TokenCardDefinitions.get("Wolf")));
        }
	};
}
