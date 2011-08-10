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

public class Recover {

	public static final MagicSpellCardEvent V5836 =new MagicSpellCardEvent("Recover") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,MagicGraveyardTargetPicker.getInstance(),
				new Object[]{cardOnStack,player},this,"Return target creature card$ from your graveyard to your hand. Draw a card.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[1];
			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicCard targetCard=event.getTarget(game,choiceResults,0);
			if (targetCard!=null) {
				game.doAction(new MagicRemoveCardAction(targetCard,MagicLocationType.Graveyard));
				game.doAction(new MagicMoveCardAction(targetCard,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
			}
			game.doAction(new MagicDrawAction(player,1));
		}
	};

}
