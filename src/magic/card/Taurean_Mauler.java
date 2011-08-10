package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Taurean_Mauler {

    public static final MagicTrigger V9112 =new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed,"Taurean Mauler") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data;
			if (cardOnStack.getController()!=player) {
				return new MagicEvent(permanent,player,new Object[]{permanent},this,"Put a +1/+1 counter on Taurean Mauler.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,1,true));
		}
    };
    
}
