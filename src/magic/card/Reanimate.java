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

public class Reanimate {

	public static final MagicSpellCardEvent V5812 =new MagicSpellCardEvent("Reanimate") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
				MagicGraveyardTargetPicker.getInstance(),new Object[]{cardOnStack,player},this,
				"Put target creature card$ from a graveyard onto the battlefield under your control. You lose life equal to its converted mana cost.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicCard targetCard=event.getTarget(game,choiceResults,0);
			if (targetCard!=null) {
				final MagicPlayer player=(MagicPlayer)data[1];
				game.doAction(new MagicReanimateAction(player,targetCard,MagicPlayCardAction.NONE));
				game.doAction(new MagicChangeLifeAction(player,-targetCard.getCardDefinition().getConvertedCost()));
			}
		}
	};
	
}
