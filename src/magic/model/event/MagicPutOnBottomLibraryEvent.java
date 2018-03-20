package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.RemoveFromPlayAction;

public class MagicPutOnBottomLibraryEvent extends MagicEvent {

    public MagicPutOnBottomLibraryEvent(final MagicPermanent permanent) {
        super(
            permanent,
            EVENT_ACTION,
            "Put SN on the bottom of its owner's library."
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        game.doAction(new RemoveFromPlayAction(event.getPermanent(),MagicLocationType.BottomOfOwnersLibrary));
}
