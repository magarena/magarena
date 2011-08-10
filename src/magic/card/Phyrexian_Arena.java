package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Phyrexian_Arena {

    public static final MagicTrigger V10159 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Phyrexian Arena") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,new Object[]{player},this,"You draw a card and you lose 1 life.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicDrawAction(player,1));
			game.doAction(new MagicChangeLifeAction(player,-1));
		}
    };
    
}
