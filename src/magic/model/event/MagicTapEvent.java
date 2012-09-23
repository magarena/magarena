package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicTapAction;

public class MagicTapEvent extends MagicEvent {
    public MagicTapEvent(final MagicPermanent permanent) {
        super(
            permanent,
            EVENT_ACTION,
            "Tap SN."
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {
            game.doAction(new MagicTapAction(event.getPermanent(),true));
        }
    };
}
