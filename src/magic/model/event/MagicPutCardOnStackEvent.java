package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.stack.MagicCardOnStack;
import magic.model.action.PutItemOnStackAction;

public class MagicPutCardOnStackEvent extends MagicEvent {
    public MagicPutCardOnStackEvent(final MagicCardOnStack source) {
        super(
            source,
            EVENT_ACTION,
            ""
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PutItemOnStackAction(event.getCardOnStack()));
        }
    };
}
