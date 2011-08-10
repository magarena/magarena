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

public class Septic_Rats {

    public static final MagicTrigger V8678 =new MagicTrigger(MagicTriggerType.WhenAttacks,"Septic Rats") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			if (permanent==data) {
				final MagicPlayer player=permanent.getController();
				if (game.getOpponent(player).getPoison()>0) {
					return new MagicEvent(permanent,player,new Object[]{permanent},this,"Septic Rats gets +1/+1 until end of turn.");
				}
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],1,1));
		}
    };
    
}
