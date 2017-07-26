package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.ShiftCardAction;
import magic.model.choice.MagicTargetChoice;

public class MagicExileCardPayManaCostEvent extends MagicEvent {

    public MagicExileCardPayManaCostEvent(final MagicSource source, final MagicTargetChoice targetChoice) {
        this(source, source.getController(), targetChoice);
    }

    public MagicExileCardPayManaCostEvent(final MagicSource source, final MagicPlayer player, final MagicTargetChoice targetChoice) {
        super(
            source,
            player,
            targetChoice,
            EVENT_ACTION,
            "Choose " + targetChoice.getTargetDescription() + "$."
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        event.processTargetCard(game, (final MagicCard card) -> {
            game.doAction(new ShiftCardAction(
                card,
                MagicLocationType.Graveyard,
                MagicLocationType.Exile
            ));
            game.addNextCostEvent(new MagicPayManaCostEvent(event.getSource(), card.getCost()));
        });
}
