package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicCard;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.stack.MagicCardOnStack;
import magic.model.action.PutItemOnStackAction;
import magic.model.action.RemoveCardAction;

public class MagicPutCardOnStackEvent extends MagicEvent {
    public MagicPutCardOnStackEvent(final MagicCard source, final MagicPlayer player, final MagicLocationType fromLocation, final MagicLocationType toLocation) {
        super(
            source,
            player,
            fromLocation.ordinal() * 10 + toLocation.ordinal(),
            EVENT_ACTION,
            ""
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        final MagicCardOnStack cardOnStack = new MagicCardOnStack(
            event.getCard(),
            event.getPlayer(),
            game.getPayedCost()
        );
        final int locations = event.getRefInt();
        final MagicLocationType from = MagicLocationType.values()[locations / 10];
        final MagicLocationType to = MagicLocationType.values()[locations % 10];
        cardOnStack.setFromLocation(from);
        cardOnStack.setMoveLocation(to);
        final MagicCard card = event.getCard();
        if (card.isToken() == false) {
            game.doAction(new RemoveCardAction(card, from));
        }
        game.doAction(new PutItemOnStackAction(cardOnStack));
    };
}
