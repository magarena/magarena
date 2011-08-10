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
