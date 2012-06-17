package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicCardOnStackAction;
import magic.model.action.MagicCopyCardOnStackAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.choice.MagicBuybackChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Reiterate {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    new MagicBuybackChoice(
                            MagicTargetChoice.TARGET_INSTANT_OR_SORCERY_SPELL,
                            MagicManaCost.THREE),
                    new Object[]{cardOnStack,cardOnStack.getController()},
                    this,
                    "Copy target instant or sorcery spell$. You may choose " +
                    "new targets for the copy. If the buyback cost was payed$, " +
                    "return " + cardOnStack + " to its owner's hand as it resolves.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
            final boolean hasTarget = event.processTargetCardOnStack(
                    game,
                    choiceResults,
                    0,
                    new MagicCardOnStackAction() {
                public void doAction(final MagicCardOnStack targetSpell) {
                    game.doAction(new MagicCopyCardOnStackAction((MagicPlayer)data[1],targetSpell));
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
