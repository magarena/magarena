package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

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
