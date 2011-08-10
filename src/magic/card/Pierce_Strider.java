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

public class Pierce_Strider {

    public static final MagicTrigger V8500 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Pierce Strider") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_OPPONENT,
				MagicEvent.NO_DATA,this,"Target opponent$ loses 3 life.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				game.doAction(new MagicChangeLifeAction(player,-3));
			}
		}		
    };
    
}
