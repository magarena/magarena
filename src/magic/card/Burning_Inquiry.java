package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;


public class Burning_Inquiry {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
            final MagicCardOnStack cardOnStack,
            final MagicPayedCost payedCost)
        {
            final MagicPlayer player = cardOnStack.getController();

            return new MagicEvent(
                cardOnStack.getCard(),
                player,
                new Object[]{cardOnStack},
                this,
                "Each player draw three cards then discard three cards."
            );
        }

        @Override
        public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] data,
            final Object[] choiceResults)
        {
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];

            game.doAction(new MagicMoveCardAction(cardOnStack));

            for (final MagicPlayer player : game.getPlayers()) {
                game.doAction(new MagicDrawAction(player, 3));
                game.addEvent(new MagicDiscardEvent(
                    cardOnStack.getCard(), player, 3, true));
            }
        }
    };
}
