package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicBuybackChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTapTargetPicker;

public class Mind_Games {
	public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    new MagicBuybackChoice(
                    		MagicTargetChoice.NEG_TARGET_ARTIFACT_OR_CREATURE_OR_LAND,
                    		MagicManaCost.TWO_BLUE),
                    new MagicTapTargetPicker(true,false),
                    new Object[]{cardOnStack},
                    this,
                    "Tap target artifact, creature, or land$. " +
                    "If the buyback cost was payed$, return " +
                    cardOnStack + " to its owner's hand as it resolves.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
			final boolean hasTarget = event.processTargetPermanent(
					game,
					choiceResults,
					0,
					new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    game.doAction(new MagicTapAction(permanent,true));
                    if (MagicBuybackChoice.isYesChoice(choiceResults[1])) {
        				game.doAction(new MagicMoveCardAction(
        						cardOnStack.getCard(),
        						MagicLocationType.Stack,
        						MagicLocationType.OwnersHand));
        			} 
                    else {
        				game.doAction(new MagicMoveCardAction(cardOnStack));
        			}
                }
			});
			if (!hasTarget) {
				game.doAction(new MagicMoveCardAction(cardOnStack));
			}
		}
	};
}
