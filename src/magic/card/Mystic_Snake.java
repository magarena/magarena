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

public class Mystic_Snake {

    public static final MagicTrigger V8254 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Mystic Snake") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_SPELL,MagicEvent.NO_DATA,this,"Counter target spell$.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				game.doAction(new MagicCounterItemOnStackAction(targetSpell));
			}
		}
    };
        
}
