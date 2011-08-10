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

public class Archon_of_Justice {

	public static final MagicTrigger V6722 =new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Archon of Justice") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_PERMANENT,MagicExileTargetPicker.getInstance(),
						MagicEvent.NO_DATA,this,"Exile target permanent$.");
			}
			return null;
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				game.doAction(new MagicRemoveFromPlayAction(permanent,MagicLocationType.Exile));
			}
		}
    };
     
}
