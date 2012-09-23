package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicUntapAction;

public class MagicUntapEvent extends MagicEvent {
    public MagicUntapEvent(final MagicPermanent permanent) {
        super(
            permanent,
            EVENT_ACTION,
            "Untap SN."
        );
    }    

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {
            game.doAction(new MagicUntapAction(event.getPermanent()));
        }        
    };
}
