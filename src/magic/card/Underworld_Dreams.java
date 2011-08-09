package magic.card;

import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;

public class Underworld_Dreams {
    public static final MagicTrigger T1 = new MagicTrigger(MagicTriggerType.WhenDrawn) {
    	@Override
    	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
    		final MagicPlayer player = ((MagicCard)data).getOwner();
    		if (permanent.getController() != player) {
    			return new MagicEvent(
    					permanent,
    					permanent.getController(),
    					new Object[]{permanent,player},
    					this,
    					"Underworld Dreams deals 1 damage to your opponent.");
    		}
    		return null;
    	}
    	
    	@Override
    	public void executeEvent(
    			final MagicGame game,
    			final MagicEvent event,
    			final Object data[],
    			final Object[] choiceResults) {
    		final MagicDamage damage = new MagicDamage((MagicSource)data[0],(MagicTarget)data[1],1,false);
    		game.doAction(new MagicDealDamageAction(damage));
    	}		
    };
}
