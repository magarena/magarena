package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.action.DiscardCardAction;

public class MagicDiscardSelfEvent extends MagicEvent {

    public MagicDiscardSelfEvent(final MagicCard card) {
        super(
            card,
            EVENT_ACTION,
            "PN discards SN."
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        game.doAction(new DiscardCardAction(event.getPlayer(), event.getCard()));
    };
}
