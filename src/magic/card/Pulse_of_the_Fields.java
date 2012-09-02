package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Pulse_of_the_Fields {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicPlayer player = cardOnStack.getController();
            final MagicCard card = cardOnStack.getCard();
            return new MagicEvent(
                    card,
                    player,
                    new Object[]{cardOnStack},
                    this,
                    player + " gains 4 life. Then if your opponent " +
                    "has more life than you, return " + 
                    card + " to its owner's hand.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicChangeLifeAction(player,4));
            final boolean more = player.getOpponent().getLife() > player.getLife();
            final MagicLocationType location = more ? MagicLocationType.OwnersHand : MagicLocationType.Graveyard;
            game.doAction(new MagicMoveCardAction(cardOnStack.getCard(),MagicLocationType.Stack,location));
        }
    };
}
