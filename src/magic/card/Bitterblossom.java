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

public class Bitterblossom {

    public static final MagicTrigger V10003 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Bitterblossom") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,new Object[]{player},this,
					"You lose 1 life and put a 1/1 black Faerie Rogue creature token with flying onto the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicChangeLifeAction(player,-1));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.FAERIE_ROGUE_TOKEN_CARD));
		}		
    };
    
}
