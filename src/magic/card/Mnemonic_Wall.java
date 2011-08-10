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

public class Mnemonic_Wall {

    public static final MagicTrigger V8084 =new MagicTrigger(MagicTriggerType.WhenComesIntoPlay,"Mnemonic Wall") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			return new MagicEvent(permanent,permanent.getController(),
                    
	new MagicMayChoice(
			"You may return target instant or sorcery card from your graveyard to your hand.",MagicTargetChoice.TARGET_INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD),
    MagicGraveyardTargetPicker.getInstance(),
				MagicEvent.NO_DATA,this,"You may$ return target instant or sorcery card$ from your graveyard to your hand.");
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
