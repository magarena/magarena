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

public class Beacon_of_Unrest {

	public static final MagicSpellCardEvent V5152 =new MagicSpellCardEvent("Beacon of Unrest") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			final MagicCard card=cardOnStack.getCard();
			return new MagicEvent(card,player,MagicTargetChoice.TARGET_ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
				MagicGraveyardTargetPicker.getInstance(),new Object[]{card,player},this,
				"Return target artifact or creature card$ from a graveyard onto the battlefield under your control. "+
				"Shuffle Beacon of Unrest into its owner's library.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCard targetCard=event.getTarget(game,choiceResults,0);
			if (targetCard!=null) {
				game.doAction(new MagicReanimateAction((MagicPlayer)data[1],targetCard,MagicPlayCardAction.NONE));
			}
			game.doAction(new MagicShuffleIntoLibraryAction((MagicCard)data[0]));
		}
	};
	
}
