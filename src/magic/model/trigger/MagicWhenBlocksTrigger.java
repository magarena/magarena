package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;

public abstract class MagicWhenBlocksTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenBlocksTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicWhenBlocksTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenBlocks;
    }
}
