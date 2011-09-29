package magic.card;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Make_a_Wish {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player = cardOnStack.getController();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    new Object[]{player,cardOnStack},
                    this,
                    "Return two cards at random from your graveyard to your hand.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPlayer player = (MagicPlayer)data[0];
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[1];
			final MagicCardList cards = player.getGraveyard();
			int actualAmount = Math.min(cards.size(),2);
			for (;actualAmount>0;actualAmount--) {
				final magic.MersenneTwisterFast rng = 
						new magic.MersenneTwisterFast(cardOnStack.getId() + player.getId());
				final int index = rng.nextInt(cards.size());
				final MagicCard card = cards.get(index);
				game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
				game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
			}
			game.doAction(new MagicMoveCardAction(cardOnStack));
		}
	};
}
