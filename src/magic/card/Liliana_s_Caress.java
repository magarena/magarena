package magic.card;
import java.util.*;
import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;
import magic.data.*;
import magic.model.variable.*;

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
