package magic.model.event;

import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.DrawAction;
import magic.model.action.ShuffleCardsIntoLibraryAction;
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
    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        final MagicPlayer player = event.getPlayer();
        if (event.isYes()) {
            final MagicCardList hand = new MagicCardList(player.getHand());
            game.doAction(new ShuffleCardsIntoLibraryAction(hand, MagicLocationType.OwnersHand));
            game.doAction(new DrawAction(player, hand.size() - 1));
            game.addEvent(new MagicMulliganEvent(player));
        }
    };
}
