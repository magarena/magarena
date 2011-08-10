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

public class Nulltread_Gargantuan {

	public static final MagicTrigger V8336 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Nulltread Gargantuan") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.CREATURE_YOU_CONTROL,
				MagicBounceTargetPicker.getInstance(),MagicEvent.NO_DATA,this,"Put a creature you control$ on top of its owner's library.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.TopOfOwnersLibrary));
			}
		}
    };
    
}
