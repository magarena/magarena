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

public class Rancor {

	public static final MagicSpellCardEvent V6520 =new MagicPlayAuraEvent("Rancor",
			MagicTargetChoice.POS_TARGET_CREATURE,MagicTrampleTargetPicker.getInstance());
    public static final MagicTrigger V10656 =new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard,"Rancor") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			if (MagicLocationType.Play==triggerData.fromLocation) {
				final MagicCard card=triggerData.card;
				return new MagicEvent(card,card.getController(),new Object[]{card},this,"Return Rancor to its owner's hand.");
			}
			return null;
		}
    	
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicCard card=(MagicCard)data[0];
			game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
			game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
		}
	};
	
}
