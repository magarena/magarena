package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.DiscardCardAction;
import magic.model.choice.MagicTargetChoice;

public class MagicDiscardChosenEvent extends MagicEvent {

    public MagicDiscardChosenEvent(final MagicSource source, final MagicTargetChoice targetChoice) {
        this(source, source.getController(), targetChoice);
    }

    public MagicDiscardChosenEvent(final MagicSource source, final MagicPlayer player, final MagicTargetChoice targetChoice) {
        super(
            source,
            player,
            targetChoice,
            EVENT_ACTION,
            "PN discards " + targetChoice.getTargetDescription().replace(" from your hand", "") + "$."
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        event.processTargetCard(game, (final MagicCard card) ->
            game.doAction(new DiscardCardAction(
                event.getPlayer(),
                card
            )));
}
