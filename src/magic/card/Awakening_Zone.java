package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

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
