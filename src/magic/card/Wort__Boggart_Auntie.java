package magic.card;

import magic.model.*;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Wort__Boggart_Auntie {

    public static final MagicTrigger V9444 =new MagicTrigger(MagicTriggerType.AtUpkeep,"Wort, Boggart Auntie") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(permanent,player,
	new MagicMayChoice(
			"You may return target Goblin card from your graveyard to your hand.",MagicTargetChoice.TARGET_GOBLIN_CARD_FROM_GRAVEYARD),
    MagicGraveyardTargetPicker.getInstance(),MagicEvent.NO_DATA,this,
					"You may$ return target Goblin card$ from your graveyard to your hand.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicCard card=event.getTarget(game,choiceResults,1);		
				if (card!=null) {
					game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
					game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
				}
			}
		}
    };
    
}
