package magic.model.trigger;

import magic.model.MagicPermanent;

public abstract class MagicWhenLoseControlTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenLoseControlTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicWhenLoseControlTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenLoseControl;
    }
}
