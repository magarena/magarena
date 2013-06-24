package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicRemoveFromPlayAction;

public class MagicExileEvent extends MagicEvent {

    public MagicExileEvent(final MagicPermanent permanent) {
        super(
            permanent,
            EVENT_ACTION,
            "Exile SN."
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicRemoveFromPlayAction(event.getPermanent(),MagicLocationType.Exile));
        }
    };
}
