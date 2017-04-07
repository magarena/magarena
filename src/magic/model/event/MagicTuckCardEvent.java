package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.DiscardCardAction;
import magic.model.choice.MagicTargetChoice;

public class MagicTuckCardEvent extends MagicEvent {

    public MagicTuckCardEvent(final MagicSource source,final MagicPlayer player) {
        super(
            source,
            player,
            MagicTargetChoice.A_CARD_FROM_HAND,
            EVENT_ACTION,
            "PN puts a card from his or her hand on the bottom of his or her library."
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        event.processTargetCard(game, (final MagicCard card) ->
            game.doAction(new DiscardCardAction(
                event.getPlayer(),
                card,
                MagicLocationType.BottomOfOwnersLibrary
            ))
        );
}
