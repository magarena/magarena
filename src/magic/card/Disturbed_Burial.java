package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicBuybackChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicGraveyardTargetPicker;

public class Disturbed_Burial {
	public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    new MagicBuybackChoice(
                    		MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                    		MagicManaCost.THREE),
                    new MagicGraveyardTargetPicker(false),
                    new Object[]{cardOnStack},
                    this,
                    "Return target creature card$ from your graveyard to your hand. " +
                    "If the buyback cost was payed$, " +
                    "return " + cardOnStack + " to its owner's hand as it resolves.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
			event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                public void doAction(final MagicCard targetCard) {
                    game.doAction(new MagicRemoveCardAction(targetCard,MagicLocationType.Graveyard));
                    game.doAction(new MagicMoveCardAction(targetCard,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                }
			});
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
