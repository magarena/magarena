package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicLocationType;
import magic.model.action.MagicChangeExtraTurnsAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicChangeCardDestinationAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Temporal_Mastery {
    public static final Object E = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    this,
                    "Take an extra turn after this one. Exile SN.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            game.doAction(new MagicChangeExtraTurnsAction(event.getPlayer(),1));
            game.doAction(new MagicChangeCardDestinationAction(event.getCardOnStack(),MagicLocationType.Exile));
        }
    };
}
