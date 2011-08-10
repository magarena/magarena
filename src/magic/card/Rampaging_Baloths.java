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

public class Rampaging_Baloths {

    public static final MagicTrigger V8563 =new MagicTrigger(MagicTriggerType.WhenOtherComesIntoPlay,"Rampaging Baloths") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicPermanent played=(MagicPermanent)data;
			if (player==played.getController()&&played.isLand()) {
				return new MagicEvent(permanent,player,new Object[]{player},this,"Put a 4/4 green Beast creature token onto the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.BEAST4_TOKEN_CARD));
		}		
    };
    
}
