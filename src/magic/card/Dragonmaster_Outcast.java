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

public class Dragonmaster_Outcast {

    public static final MagicTrigger V7135 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Dragonmaster Outcast") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			if (player==data&&player.getNrOfPermanentsWithType(MagicType.Land)>=6) {
				return new MagicEvent(permanent,player,new Object[]{player},this,"You put a 5/5 red Dragon creature token with flying onto the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.DRAGON5_TOKEN_CARD));
		}
    };
    
}
