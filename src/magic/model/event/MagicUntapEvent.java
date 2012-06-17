package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicUntapAction;

public class MagicUntapEvent extends MagicEvent {

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
    
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {

            game.doAction(new MagicUntapAction((MagicPermanent)data[0]));
        }        
    };
        
    public MagicUntapEvent(final MagicPermanent permanent) {
        
        super(permanent,permanent.getController(),new Object[]{permanent},EVENT_ACTION,"Untap "+permanent.getName()+".");
    }    
}