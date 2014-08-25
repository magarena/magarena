package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDiscardCardAction;
import magic.model.trigger.MagicWhenCycleTrigger;

public class MagicDiscardSelfEvent extends MagicEvent {

    public MagicDiscardSelfEvent(final MagicCard card) {
        super(
            card,
            EVENT_ACTION,
            "PN discards SN."
        );
    }

    private static final MagicEventAction EVENT_ACTION = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDiscardCardAction(event.getPlayer(), event.getCard()));
        }
    };
}
