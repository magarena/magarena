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

public class Tuktuk_the_Explorer {

    public static final MagicTrigger V9252 =new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Tuktuk the Explorer") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
		
			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,
					"You put a legendary 5/5 colorless Goblin Golem artifact creature token named Tuktuk the Returned onto the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.TUKTUK_THE_RETURNED_TOKEN_CARD));
		}
    };

}
