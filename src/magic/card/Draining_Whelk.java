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

public class Draining_Whelk {

    public static final MagicTrigger V7154 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Draining Whelk") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_SPELL,new Object[]{permanent},this,
				"Counter target spell$. Put X +1/+1 counters on Draining Whelk, where X is that spell's converted mana cost.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicCardOnStack card=event.getTarget(game,choiceResults,0);
			if (card!=null) {
				game.doAction(new MagicCounterItemOnStackAction(card));
				game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,card.getConvertedCost(),true));
			}
		}
    };

}
