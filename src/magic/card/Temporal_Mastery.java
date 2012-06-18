package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicLocationType;
import magic.model.action.MagicChangeExtraTurnsAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Temporal_Mastery {
    public static final Object E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    new Object[]{cardOnStack},
                    this,
                    "Take an extra turn after this one. Exile " + cardOnStack.getCard());
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
            cardOnStack.setMoveLocation(MagicLocationType.Exile);
            game.doAction(new MagicChangeExtraTurnsAction(event.getPlayer(),1));
            game.doAction(new MagicMoveCardAction(cardOnStack));
        }
    };
}
