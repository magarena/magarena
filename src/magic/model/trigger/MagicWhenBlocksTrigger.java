package magic.model.trigger;

import magic.model.MagicPermanent;

public abstract class MagicWhenBlocksTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenBlocksTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicWhenBlocksTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenBlocks;
    }
}
