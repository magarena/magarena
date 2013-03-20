package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.action.MagicCardOnStackAction;
import magic.model.action.MagicCounterItemOnStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Dismal_Failure {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    MagicTargetChoice.NEG_TARGET_SPELL,
                    this,
                    "Counter target spell$. Its controller discards a card.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetCardOnStack(game,choiceResults,new MagicCardOnStackAction() {
                public void doAction(final MagicCardOnStack counteredCard) {
                    game.doAction(new MagicCounterItemOnStackAction(counteredCard));
                    game.addEvent(new MagicDiscardEvent(event.getSource(),counteredCard.getController(),1,false));
                }
            });
        }
    };
}
