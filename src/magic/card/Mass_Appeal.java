package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Mass_Appeal {
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                    cardOnStack,
                    this,
                    "PN draws a card for each Human he or she controls.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPlayer player = event.getPlayer();
            game.doAction(new MagicDrawAction(
                    player,
                    player.getNrOfPermanentsWithSubType(MagicSubType.Human)));
        }
    };
}
