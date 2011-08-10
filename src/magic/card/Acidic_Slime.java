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

public class Acidic_Slime {

	public static final MagicTrigger V6665 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Acidic Slime") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),MagicTargetChoice.TARGET_ARTIFACT_OR_ENCHANTMENT_OR_LAND,
				new MagicDestroyTargetPicker(false),MagicEvent.NO_DATA,this,"Destroy target artifact, enchantment or land$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				game.doAction(new MagicDestroyAction(permanent));
			}
		}
    };
	
}
