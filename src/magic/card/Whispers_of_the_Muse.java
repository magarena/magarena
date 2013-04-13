package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.action.MagicChangeCardDestinationAction;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicBuybackChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Whispers_of_the_Muse {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicBuybackChoice(MagicManaCost.create("{5}")),
                this,
                "PN draws a card. If the buyback cost was payed$, " +
                "return SN to its owner's hand as it resolves."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicDrawAction(event.getPlayer(),1));
            if (event.isBuyback()) {
                game.doAction(new MagicChangeCardDestinationAction(event.getCardOnStack(), MagicLocationType.OwnersHand));
            } 
        }
    };
}
