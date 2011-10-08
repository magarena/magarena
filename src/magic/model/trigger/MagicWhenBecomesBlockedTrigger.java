package magic.model.trigger;

import magic.model.MagicPermanent;

public abstract class MagicWhenBecomesBlockedTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenBecomesBlockedTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicWhenBecomesBlockedTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenBecomesBlocked;
    }
}
