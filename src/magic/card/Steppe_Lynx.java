package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Steppe_Lynx {

    public static final MagicTrigger V8809 =new MagicTrigger(MagicTriggerType.WhenOtherComesIntoPlay,"Steppe Lynx") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicPermanent played=(MagicPermanent)data;
			if (player==played.getController()&&played.isLand()) {
				return new MagicEvent(permanent,player,new Object[]{permanent},this,"Steppe Lynx gets +2/+2 until end of turn.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],2,2));
		}		
    };
    
}
