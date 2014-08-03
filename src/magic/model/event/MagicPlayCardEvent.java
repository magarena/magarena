package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.action.MagicPlayMod;
import magic.model.action.MagicPlayCardFromStackAction;
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
    
    public static final MagicSpellCardEvent MORPH = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Put a face-down creature onto the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayCardFromStackAction(event.getCardOnStack(), MagicPlayMod.FACE_DOWN));
        }
    };
}
