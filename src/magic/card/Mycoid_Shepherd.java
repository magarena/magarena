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

public class Mycoid_Shepherd {

    public static final MagicTrigger V8181 =new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Mycoid Shepherd") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
		
			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,"You gain 5 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],5));
		}
    };

    public static final MagicTrigger V8201 =new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay,"Mycoid Shepherd") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicPermanent otherPermanent=(MagicPermanent)data;
			if (otherPermanent!=permanent&&otherPermanent.getController()==player&&otherPermanent.isCreature()&&otherPermanent.getPower(game)>=5) {			
				return new MagicEvent(permanent,player,new Object[]{player},this,"You gain 5 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],5));
		}
    };
    
}
