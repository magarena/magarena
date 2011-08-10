package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Liliana_s_Caress {

    public static final MagicTrigger V7867 =new MagicTrigger(MagicTriggerType.WhenDiscarded,"Liliana's Caress") {

    	@Override
    	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
    		final MagicPlayer otherController = ((MagicCard)data).getOwner();
    		final MagicPlayer player = permanent.getController();
    		if (otherController != player) {		
    			return new MagicEvent(permanent,player,new Object[]{otherController},this,"Your opponent loses 2 life.");
    		}
    		return null;
    	}

    	@Override
    	public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
    		game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],-2));
    	}
    };
    	    
}
