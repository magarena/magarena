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

public class Affa_Guard_Hound {

    public static final MagicTrigger V6684 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Affa Guard Hound") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_CREATURE,MagicPumpTargetPicker.getInstance(),
				MagicEvent.NO_DATA,this,"Target creature$ gets +0/+3 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,0,3));
			}
		}
    };
	
}
