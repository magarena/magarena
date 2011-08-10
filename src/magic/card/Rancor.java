package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicTrampleTargetPicker;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

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
