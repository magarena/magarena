package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Pulse_of_the_Tangle {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player=cardOnStack.getController();
            final MagicCard card = cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    player,
                    new Object[]{cardOnStack,player},
                    this,
                    player + " puts a 3/3 green Beast creature token onto the battlefield. " + 
                    "Then if your opponent controls more creatures than you, return " + card + " to its owner's hand.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			final MagicPlayer player=(MagicPlayer)data[1];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.get("Beast3")));
			final boolean more=game.getOpponent(player).getNrOfPermanentsWithType(MagicType.Creature)>
			player.getNrOfPermanentsWithType(MagicType.Creature);			
			final MagicLocationType location=more?MagicLocationType.OwnersHand:MagicLocationType.Graveyard;
			game.doAction(new MagicMoveCardAction(cardOnStack.getCard(),MagicLocationType.Stack,location));
		}
	};
}
