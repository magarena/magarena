package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.stack.MagicCardOnStack;
import magic.model.action.PlayCardFromStackAction;

public class MagicPlayCardEvent {
    private MagicPlayCardEvent() {}

    public static MagicETBEvent create() {
        return INSTANCE;
    }

    private static final MagicETBEvent INSTANCE = new MagicETBEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Put SN onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayCardFromStackAction(
                event.getCardOnStack()
            ));
        }
    };
}
