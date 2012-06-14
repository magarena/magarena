package magic.card;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayerAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;


public class Wheel_of_Fortune {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack.getCard(),
                cardOnStack.getController(),
                new Object[]{cardOnStack},
                this,
                "Each player discards his or her hand and draws seven cards."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event, final Object[] data, final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];

            game.doAction(new MagicMoveCardAction(cardOnStack));

            for (final MagicPlayer player : game.getPlayers()) {
                game.addEvent(new MagicDiscardEvent(cardOnStack.getCard(), player, player.getHandSize(), true));
                game.doAction(new MagicDrawAction(player, 7));
            }
        }
    };
}


