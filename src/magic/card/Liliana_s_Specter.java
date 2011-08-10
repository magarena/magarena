package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Liliana_s_Specter {

    public static final MagicTrigger V7885 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Liliana's Specter") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
						
			return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,"Your opponent discards a card.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			game.addEvent(new MagicDiscardEvent(permanent,game.getOpponent(permanent.getController()),1,false));
		}		
    };
    
}
