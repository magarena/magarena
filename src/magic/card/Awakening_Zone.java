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

public class Awakening_Zone {

    public static final MagicTrigger V9982 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Awakening Zone") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,new Object[]{player},this,
					"You put a 0/1 colorless Eldrazi Spawn creature token onto the battlefield. "+
					"It has \"Sacrifice this creature: Add {1} to your mana pool.\"");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.ELDRAZI_SPAWN_TOKEN_CARD));
		}		
    };
    
}
