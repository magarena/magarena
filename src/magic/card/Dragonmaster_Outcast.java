package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

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
