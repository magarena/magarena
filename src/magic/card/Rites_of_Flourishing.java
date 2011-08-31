package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLandPlayedAction;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Rites_of_Flourishing {
	public static final MagicTrigger T1 = new MagicTrigger(MagicTriggerType.WhenComesIntoPlay) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player = permanent.getController();
			return new MagicEvent(
                    permanent,
                    player,
                    MagicEvent.NO_DATA,
                    this,
                    player + " may play an additional land this turn.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeLandPlayedAction(-1));
		}
    };
    
    public static final MagicTrigger T2 = new MagicTrigger(MagicTriggerType.AtUpkeep) {
    	@Override
    	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
    		final MagicPlayer player = (MagicPlayer)data;
    			return new MagicEvent(
    					permanent,
    					permanent.getController(),
    					MagicEvent.NO_DATA,
    					this,
    					player + " may play an additional land this turn.");
    	}
    	
    	@Override
    	public void executeEvent(
    			final MagicGame game,
    			final MagicEvent event,
    			final Object data[],
    			final Object[] choiceResults) {
            game.doAction(new MagicChangeLandPlayedAction(-1));
    	}		
    };
    
	public static final MagicTrigger T3 = new MagicTrigger(MagicTriggerType.AtUpkeep) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player = (MagicPlayer)data;
				return new MagicEvent(
						permanent,
						player,
						new Object[]{player},
						this,
						player + " draws a card.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {	
			final MagicPlayer player = (MagicPlayer)data[0];
			game.doAction(new MagicDrawAction(player,1));
		}
    };
}
