package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;

public abstract class MagicWhenDiesTrigger extends MagicWhenPutIntoGraveyardTrigger {
    public MagicWhenDiesTrigger(final int priority) {
        super(priority); 
    }
    
    public MagicWhenDiesTrigger() {}

    protected abstract MagicEvent getEvent(final MagicPermanent permanent);
    
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
        return (triggerData.fromLocation == MagicLocationType.Play) ?
            getEvent(permanent) : MagicEvent.NONE;
    }
}
