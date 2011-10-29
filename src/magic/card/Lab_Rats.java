package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.choice.MagicBuybackChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Lab_Rats {
	public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player = cardOnStack.getController();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    new MagicBuybackChoice(MagicManaCost.FOUR),
                    new Object[]{cardOnStack,player},
                    this,
                    player + " puts a 1/1 black Rat creature token onto the " +
                    "battlefield. If the buyback cost was payed$, " +
                    "return " + cardOnStack + " to its owner's hand as it resolves.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
			game.doAction(new MagicPlayTokenAction(
					(MagicPlayer)data[1],
					TokenCardDefinitions.getInstance().getTokenDefinition("Rat1")));
			if (MagicBuybackChoice.isYesChoice(choiceResults[1])) {
				game.doAction(new MagicMoveCardAction(
						cardOnStack.getCard(),
						MagicLocationType.Stack,
						MagicLocationType.OwnersHand));
			} else {
				game.doAction(new MagicMoveCardAction(cardOnStack));
			}
		}
	};
}
