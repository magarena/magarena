package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Sangromancer {

    public static final MagicTrigger V8640 =new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,"Sangromancer") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player = permanent.getController();
			final MagicPermanent otherPermanent = (MagicPermanent)data;
			final MagicPlayer otherController = otherPermanent.getController();
			if (otherController != player && otherPermanent.isCreature()) {			
				return new MagicEvent(permanent,player,new Object[]{player},this,"You gain 3 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],3));
		}
    };
    
    public static final MagicTrigger V8660 =new MagicTrigger(MagicTriggerType.WhenDiscarded,"Sangromancer") {

    	@Override
    	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
    		final MagicPlayer otherController = ((MagicCard)data).getOwner();
    		final MagicPlayer player = permanent.getController();
    		if (otherController != player) {		
    			return new MagicEvent(permanent,player,new Object[]{player},this,"You gain 3 life.");
    		}
    		return null;
    	}

    	@Override
    	public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
    		game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],3));
    	}
    };
    
}
