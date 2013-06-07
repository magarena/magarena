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
            game.doAction(new MagicPlayCardFromStackAction(event.getCardOnStack()));
        }
    };

    public static MagicSpellCardEvent createKicker(final MagicManaCost kickerCost, final boolean multi, final String desc) {
        return new MagicSpellCardEvent() {
            @Override
            public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
                return new MagicEvent(
                    cardOnStack,
                    new MagicKickerChoice(kickerCost,multi),
                    this,
                    "$Play SN. If SN was kicked$, " + desc + "."
                );
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicPlayCardFromStackAction(event.getCardOnStack(), event.getKickerCount()));
            }
        };
    }
    
    public static MagicSpellCardEvent createX(final String desc) {
        return new MagicSpellCardEvent() {
            @Override
            public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
                return new MagicEvent(
                    cardOnStack,
                    payedCost.getX(),
                    this,
                    "$Play SN. " + desc + "."
                );
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                final int X = event.getRefInt();
                game.doAction(new MagicPlayCardFromStackAction(event.getCardOnStack(), X));
            }
        };
    }
}
