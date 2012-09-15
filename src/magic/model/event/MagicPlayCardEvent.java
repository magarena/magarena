package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicCard;
import magic.model.MagicManaCost;
import magic.model.choice.MagicKickerChoice;
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
                    "Put " + cardOnStack.getName() + " onto the battlefield.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choices) {
            game.doAction(new MagicPlayCardFromStackAction(event.getCardOnStack()));
        }
    };

    public static MagicSpellCardEvent createKicker(final MagicManaCost kickerCost, final boolean multi, final String desc) {
        return new MagicSpellCardEvent() {
            @Override
            public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
                final MagicCard card = cardOnStack.getCard();
                return new MagicEvent(
                        cardOnStack,
                        new MagicKickerChoice(kickerCost,multi),
                        this,
                        "$Play " + card + ". If " + card + " was kicked$, " + desc + ".");
            }
            @Override
            public void executeEvent(
                    final MagicGame game,
                    final MagicEvent event,
                    final Object[] data,
                    final Object[] choiceResults) {
                final int kicker = (Integer)choiceResults[1];
                game.doAction(new MagicPlayCardFromStackAction(event.getCardOnStack(), kicker));
            }
        };
    }
    
    public static MagicSpellCardEvent createX(final String desc) {
        return new MagicSpellCardEvent() {
            @Override
            public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
                final MagicCard card = cardOnStack.getCard();
                return new MagicEvent(
                        cardOnStack,
                        new Object[]{payedCost.getX()},
                        this,
                        "$Play " + card + ". " + desc + ".");
            }
            @Override
            public void executeEvent(
                    final MagicGame game,
                    final MagicEvent event,
                    final Object[] data,
                    final Object[] choiceResults) {
                final int X = (Integer)data[0];
                game.doAction(new MagicPlayCardFromStackAction(event.getCardOnStack(), X));
            }
        };
    }
}
