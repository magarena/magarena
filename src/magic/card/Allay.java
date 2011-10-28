package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicBuybackChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDestroyTargetPicker;

public class Allay {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    new MagicBuybackChoice(
                    		MagicTargetChoice.NEG_TARGET_ENCHANTMENT,
                    		MagicManaCost.THREE),     
                    new MagicDestroyTargetPicker(false),
                    new Object[]{cardOnStack},
                    this,
                    "Destroy target enchantment$. If the buyback cost was " +
                    "payed$, return " + cardOnStack + " to its owner's " +
                    "hand as it resolves.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                	game.doAction(new MagicDestroyAction(permanent));
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
