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

public class Pulse_of_the_Tangle {

	public static final MagicSpellCardEvent V5788 =new MagicSpellCardEvent("Pulse of the Tangle") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,new Object[]{cardOnStack,player},this,
				"You put a 3/3 green Beast creature token onto the battlefield. "+
				"Then if your opponent controls more creatures than you, return Pulse of the Tangle to its owner's hand.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			final MagicPlayer player=(MagicPlayer)data[1];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.BEAST3_TOKEN_CARD));
			final boolean more=game.getOpponent(player).getNrOfPermanentsWithType(MagicType.Creature)>
			    player.getNrOfPermanentsWithType(MagicType.Creature);			
			final MagicLocationType location=more?MagicLocationType.OwnersHand:MagicLocationType.Graveyard;
			game.doAction(new MagicMoveCardAction(cardOnStack.getCard(),MagicLocationType.Stack,location));
		}
	};
	
}
