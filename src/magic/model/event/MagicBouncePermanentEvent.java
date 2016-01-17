package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.RemoveFromPlayAction;

public class MagicBouncePermanentEvent extends MagicEvent {

    public MagicBouncePermanentEvent(final MagicSource source, final MagicPermanent perm) {
        super(
            source,
            perm,
            EVENT_ACTION,
            "Return SN to its owner's hand."
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        game.doAction(new RemoveFromPlayAction(event.getRefPermanent(), MagicLocationType.OwnersHand));
}
