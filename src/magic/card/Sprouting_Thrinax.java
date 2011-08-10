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

public class Sprouting_Thrinax {

    public static final MagicTrigger V9028 =new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Sprouting Thrinax") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
		
			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,"You put three 1/1 green Saproling creature tokens onto the battlefield.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			final MagicPlayer player=(MagicPlayer)data[0];
			for (int count=3;count>0;count--) {
				
				game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.SAPROLING_TOKEN_CARD));
			}
		}
    };
    
}
