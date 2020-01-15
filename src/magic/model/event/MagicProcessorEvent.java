package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicSource;
import magic.model.action.ShiftCardAction;
import magic.model.choice.MagicTargetChoice;

public class MagicProcessorEvent extends MagicEvent {

    public MagicProcessorEvent(final MagicSource source) {
        super(
            source,
            MagicTargetChoice.A_CARD_FROM_OPPONENTS_EXILE,
            EVENT_ACTION,
            "PN puts a card an opponent owns from exile$ into that player's graveyard."
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        event.processTargetCard(game, (final MagicCard card) -> {
            game.doAction(new ShiftCardAction(
                card,
                MagicLocationType.Exile,
                MagicLocationType.Graveyard
            ));
        });
    };
}
