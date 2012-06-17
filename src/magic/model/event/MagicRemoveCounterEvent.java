package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;

public class MagicRemoveCounterEvent extends MagicEvent {

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {

        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {
            final int amount=(Integer)data[2];
            game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],(MagicCounterType)data[1],-amount,true));
        }        
    };
        
    public MagicRemoveCounterEvent(final MagicPermanent permanent,final MagicCounterType counterType,final int amount) {
        super(
            permanent,
            permanent.getController(),
            new Object[]{permanent,counterType,amount},
            EVENT_ACTION,
            genDescription(permanent,counterType,amount));
    }    
    
    private static String genDescription(final MagicPermanent permanent,final MagicCounterType counterType,final int amount) {
        final StringBuilder description=new StringBuilder("Remove ");
        if (amount==1) {
            description.append("a ").append(counterType.getName()).append(" counter");
        } else {
            description.append(amount).append(' ').append(counterType.getName()).append(" counters");
        }        
        description.append(" from ").append(permanent.getName()).append('.');
        return description.toString();
    }
}
