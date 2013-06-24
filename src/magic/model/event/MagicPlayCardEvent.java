package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.stack.MagicCardOnStack;

public class MagicPlayCardEvent {
    private MagicPlayCardEvent() {}

    public static MagicSpellCardEvent create() {
        return INSTANCE;
    }

    private static final MagicSpellCardEvent INSTANCE = new MagicSpellCardEvent() {
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
            game.doAction(new MagicPlayCardFromStackAction(
                event.getCardOnStack()
            ));
        }
    };
}
