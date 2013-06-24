package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicLocationType;
import magic.model.action.MagicRemoveFromPlayAction;

public class MagicBouncePermanentEvent extends MagicEvent {

    public MagicBouncePermanentEvent(final MagicSource source, final MagicPermanent perm) {
        super(
            source,
            perm,
            EVENT_ACTION,
            "Return SN to its owner's hand."
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicRemoveFromPlayAction(event.getRefPermanent(),MagicLocationType.OwnersHand));
        }
    };
}
