package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.RemoveFromPlayAction;

public class MagicExileEvent extends MagicEvent {

    public MagicExileEvent(final MagicPermanent permanent) {
        super(
            permanent,
            EVENT_ACTION,
            "Exile SN."
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        game.doAction(new RemoveFromPlayAction(event.getPermanent(),MagicLocationType.Exile));
}
