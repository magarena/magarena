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

public class Kokusho__the_Evening_Star {

    public static final MagicTrigger V7779 =new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Kokusho, the Evening Star") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
		
			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,"Your opponent loses 5 life. You gain 5 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicChangeLifeAction(game.getOpponent(player),-5));
			game.doAction(new MagicChangeLifeAction(player,5));
		}
    };

}
