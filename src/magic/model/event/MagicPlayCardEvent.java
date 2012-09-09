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
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    new Object[]{cardOnStack},
                    this,
                    "Put " + cardOnStack.getName() + " onto the battlefield.");
        }

        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {
            final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
            if (choices.length >= 2 && choices[1] != null && choices[1] instanceof Integer) { 
                game.doAction(new MagicPlayCardFromStackAction(cardOnStack, (Integer)choices[1]));
            } else {
                game.doAction(new MagicPlayCardFromStackAction(cardOnStack));
            }
        }
    };
    

    public static MagicSpellCardEvent create(final MagicManaCost kickerCost, final boolean multi, final String desc) {
        return new MagicSpellCardEvent() {
            @Override
            public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
                final MagicPlayer player = cardOnStack.getController();
                final MagicCard card = cardOnStack.getCard();
                return new MagicEvent(
                        card,
                        player,
                        new MagicKickerChoice(kickerCost,multi),
                        new Object[]{cardOnStack},
                        this,
                        "$Play " + card + ". If " + card + " was kicked$, " + desc + ".");
            }
            @Override
            public void executeEvent(
                    final MagicGame game,
                    final MagicEvent event,
                    final Object[] data,
                    final Object[] choiceResults) {
                final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
                final MagicPlayCardFromStackAction action = new MagicPlayCardFromStackAction(cardOnStack, (Integer)choiceResults[1]);
                game.doAction(action);
            }
        };
    }
}
