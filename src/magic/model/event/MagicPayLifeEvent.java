package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicChangeLifeAction;

public class MagicPayLifeEvent extends MagicEvent {

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
    
        
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choices) {
            
            game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],(Integer)data[1]));
        }
    };

    public MagicPayLifeEvent(final MagicSource source,final MagicPlayer player,final int amount) {
        
        super(source,player,new Object[]{player,-amount},EVENT_ACTION,"Pay "+amount+" life.");        
    }    
}