package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicCard;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.stack.MagicCardOnStack;
import magic.model.action.PutItemOnStackAction;

public class MagicPutCardOnStackEvent extends MagicEvent {
    public MagicPutCardOnStackEvent(final MagicCard source, final MagicPlayer player, final MagicLocationType fromLocation) {
        super(
            source,
            player,
            fromLocation.ordinal(),
            EVENT_ACTION,
            ""
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardOnStack cardOnStack = new MagicCardOnStack(
                event.getCard(),
                event.getPlayer(),
                game.getPayedCost()
            );
            cardOnStack.setFromLocation(MagicLocationType.values()[event.getRefInt()]);
            game.doAction(new PutItemOnStackAction(cardOnStack));
        }
    };
}
