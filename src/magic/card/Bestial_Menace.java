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

public class Bestial_Menace {

	public static final MagicSpellCardEvent V5176 =new MagicSpellCardEvent("Bestial Menace") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player},this,
				"Put a 1/1 green Snake creature token, a 2/2 green Wolf creature token and a 3/3 green Elephant creature token onto the battlefield.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player=(MagicPlayer)data[1];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.SNAKE_TOKEN_CARD));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.WOLF_TOKEN_CARD));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.ELEPHANT_TOKEN_CARD));
		}
	};
	
}
