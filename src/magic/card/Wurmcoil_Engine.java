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

public class Wurmcoil_Engine {

    public static final MagicTrigger V9507 =new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Wurmcoil Engine") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
		
			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,
					"You put a 3/3 colorless Wurm artifact creature token with deathtouch and "+
					"a 3/3 colorless Wurm artifact creature token with lifelink onto the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.WURM1_TOKEN_CARD));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.WURM2_TOKEN_CARD));			
		}
    };
    
}
