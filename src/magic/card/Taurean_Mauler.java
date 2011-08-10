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
