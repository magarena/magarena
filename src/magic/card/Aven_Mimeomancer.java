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

public class Aven_Mimeomancer {

    public static final MagicTrigger V6785 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Aven Mimeomancer") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent.getController()==data) {
				return new MagicEvent(permanent,permanent.getController(),
	new MagicMayChoice(
			"You may put a feather counter on target creature.",MagicTargetChoice.TARGET_CREATURE),
    new MagicBecomeTargetPicker(3,1,true),
					MagicEvent.NO_DATA,this,"You may$ put a feather counter on target creature$.");
			}
			return null;
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPermanent creature=event.getTarget(game,choiceResults,1);
				if (creature!=null) {
					game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.Feather,1,true));
				}
			}
		}
    };
    
}
