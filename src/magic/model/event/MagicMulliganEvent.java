package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicMulliganChoice;

public class MagicMulliganEvent extends MagicEvent {

    public MagicMulliganEvent(final MagicPlayer player) {
        super(
            MagicSource.NONE,
            player,
            new MagicMulliganChoice(),
            EVENT_ACTION,
            "PN may$ take a mulligan."
        );
    }
    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            if (event.isYes()) {
                final MagicCardList hand = new MagicCardList(player.getHand());
                final int size = hand.size();
                for (final MagicCard card : hand) {
                    game.doAction(new MagicMoveCardAction(
                        card,
                        MagicLocationType.OwnersHand,
                        MagicLocationType.OwnersLibrary
                    ));
                    game.doAction(new MagicRemoveCardAction(
                        card,
                        MagicLocationType.OwnersHand
                    ));
                }
                final MagicCardList library = player.getLibrary();
                library.shuffle();
                game.doAction(new MagicDrawAction(player,size - 1));
                game.addEvent(new MagicMulliganEvent(player));
            }
        }
    };
}
