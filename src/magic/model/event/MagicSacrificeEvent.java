package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicSacrificeAction;

public class MagicSacrificeEvent extends MagicEvent {

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
    
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {

            game.doAction(new MagicSacrificeAction((MagicPermanent)data[0]));
        }
    };
        
    public MagicSacrificeEvent(final MagicPermanent permanent) {
        
        super(permanent,permanent.getController(),new Object[]{permanent},EVENT_ACTION,"Sacrifice "+permanent.getName()+".");
    }    
}