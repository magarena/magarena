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

public class Sylvok_Lifestaff {

    public static final MagicTrigger V9857 =new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,"Sylvok Lifestaff") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent.getEquippedCreature()==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,"You gain 3 life.");
			}			
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],3));
		}
    };
    
    
}
