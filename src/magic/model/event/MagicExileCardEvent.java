package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicCardAction;
import magic.model.action.ShiftCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicExileCardEvent extends MagicEvent {

    public MagicExileCardEvent(final MagicSource source, final MagicTargetChoice targetChoice) {
        this(source, source.getController(), targetChoice);
    }

    public MagicExileCardEvent(final MagicSource source, final MagicPlayer player, final MagicTargetChoice targetChoice) {
        super(
            source,
            player,
            targetChoice,
            EVENT_ACTION,
            "Exile " + targetChoice.getTargetDescription() + "$."
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        event.processTargetCard(game, (final MagicCard card) -> {
            final MagicLocationType fromLocation=card.getLocation();
            game.doAction(new ShiftCardAction(
                card,
                fromLocation,
                MagicLocationType.Exile
            ));
        });
    };
}
