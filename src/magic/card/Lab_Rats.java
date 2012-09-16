package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCardDestinationAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.choice.MagicBuybackChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Lab_Rats {
    public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    new MagicBuybackChoice(MagicManaCost.FOUR),
                    this,
                    "PN puts a 1/1 black Rat creature token onto the " +
                    "battlefield. If the buyback cost was payed$, " +
                    "return SN to its owner's hand as it resolves.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            game.doAction(new MagicPlayTokenAction(
                    event.getPlayer(),
                    TokenCardDefinitions.get("Rat1")));
            if (MagicBuybackChoice.isYesChoice(choiceResults[1])) {
                game.doAction(new MagicChangeCardDestinationAction(event.getCardOnStack(), MagicLocationType.OwnersHand));
            } 
        }
    };
}
