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

public class Dragon_Fodder {

	public static final MagicSpellCardEvent V5466 =new MagicSpellCardEvent("Dragon Fodder") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player},this,
				"Put two 1/1 red Goblin creature tokens onto the battlefield.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player=(MagicPlayer)data[1];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.GOBLIN1_TOKEN_CARD));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.GOBLIN1_TOKEN_CARD));
		}
	};
	
}
