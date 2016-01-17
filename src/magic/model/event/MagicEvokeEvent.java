package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.action.SetKickerAction;

public class MagicEvokeEvent extends MagicEvent {

    public MagicEvokeEvent(final MagicSource source) {
        super(
            source,
            EVENT_ACTION,
            ""
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        game.doAction(new SetKickerAction(1));
}
