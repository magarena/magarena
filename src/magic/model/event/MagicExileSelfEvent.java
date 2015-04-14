package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.action.MoveCardAction;
import magic.model.action.RemoveCardAction;

public class MagicExileSelfEvent extends MagicEvent {

    public MagicExileSelfEvent(final MagicCard card, final MagicLocationType fromLocation) {
        super(
            card,
            fromLocation.ordinal(),
            EVENT_ACTION,
            "PN exiles SN."
        );
    }

    private static final MagicEventAction EVENT_ACTION = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getCard();
            final MagicLocationType fromLocation = MagicLocationType.values()[event.getRefInt()];
            game.doAction(new RemoveCardAction(
                card,
                fromLocation
            ));
            game.doAction(new MoveCardAction(
                card,
                fromLocation,
                MagicLocationType.Exile
            ));
        }
    };
}
