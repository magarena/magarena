package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicPayManaCostChoice;

public class MagicPayManaCostTapEvent extends MagicEvent {
    
    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
            
            MagicEvent.payManaCost(game,(MagicPlayer)data[0],choiceResults,0);
            game.doAction(new MagicTapAction((MagicPermanent)data[1],true));
        }        
    };
    
    public MagicPayManaCostTapEvent(final MagicSource source,final MagicPlayer player,final MagicManaCost cost) {
        
        super(source,player,new MagicPayManaCostChoice(cost),new Object[]{player,source},EVENT_ACTION,
            "Pay "+cost.getText()+"$. Tap "+source.getName()+".");
    }            
}
