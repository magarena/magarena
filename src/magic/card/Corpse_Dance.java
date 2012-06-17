package magic.card;

import java.util.List;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.choice.MagicBuybackChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

public class Corpse_Dance {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    new MagicBuybackChoice(MagicManaCost.TWO),
                    new MagicGraveyardTargetPicker(true),
                    new Object[]{cardOnStack,cardOnStack.getController()},
                    this,
                    "Return the top creature card$ of your graveyard to the battlefield. " +
                    "That creature gains haste until end of turn. Exile it at the " +
                    "beginning of the next end step. If the buyback cost was payed$, " +
                    "return " + cardOnStack + " to its owner's hand as it resolves.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
            final MagicPlayer player = (MagicPlayer)data[1];
            final List<MagicTarget> targets =
                    game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE_CARD_FROM_GRAVEYARD);
            if (targets.size() > 0) {
                final MagicCard card = (MagicCard)targets.get(targets.size()-1);
                game.doAction(new MagicReanimateAction(
                        (MagicPlayer)data[1],
                        card,
                        MagicPlayCardAction.HASTE_UEOT_REMOVE_AT_END_OF_TURN));
                if (MagicBuybackChoice.isYesChoice(choiceResults[1])) {
                    game.doAction(new MagicMoveCardAction(
                            cardOnStack.getCard(),
                            MagicLocationType.Stack,
                            MagicLocationType.OwnersHand));
                } else {
                    game.doAction(new MagicMoveCardAction(cardOnStack));
                }
            } else {
                game.doAction(new MagicMoveCardAction(cardOnStack));
            }
            
        }
    };
}
