package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;

public abstract class MagicWhenOtherPutIntoGraveyardFromPlayTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenOtherPutIntoGraveyardFromPlayTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay;
    }
}
