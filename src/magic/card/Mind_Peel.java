package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayerAction;
import magic.model.choice.MagicBuybackChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Mind_Peel {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    new MagicBuybackChoice(
                            MagicTargetChoice.NEG_TARGET_PLAYER,
                            MagicManaCost.TWO_BLACK_BLACK),
                    new Object[]{cardOnStack},
                    this,
                    "Target player$ discards a card. " +
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
            final boolean hasTarget = event.processTargetPlayer(
                    game,
                    choiceResults,
                    0,
                    new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    game.addEvent(new MagicDiscardEvent(cardOnStack.getCard(),player,1,false));
                    if (MagicBuybackChoice.isYesChoice(choiceResults[1])) {
                        game.doAction(new MagicMoveCardAction(
                                cardOnStack.getCard(),
                                MagicLocationType.Stack,
                                MagicLocationType.OwnersHand));
                    } else {
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
