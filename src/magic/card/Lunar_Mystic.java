package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicWhenOtherSpellIsCastTrigger;

public class Lunar_Mystic {
    public static final MagicWhenOtherSpellIsCastTrigger T = new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicCardOnStack data) {
            final MagicPlayer player = permanent.getController();
            final MagicCard card = data.getCard();
            return (card.getOwner() == player &&
                    data.getCardDefinition().isInstant()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                                "You may pay {1}.",
                                new MagicPayManaCostChoice(MagicManaCost.ONE)),
                        this,
                        player + " may$ pay {1}$. If you do, draw a card."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicDrawAction(event.getPlayer(),1));
            }
        }
    };
}
