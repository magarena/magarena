package magic.model.trigger;

import magic.model.MagicPermanent;

public abstract class MagicWhenLeavesPlayTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenLeavesPlayTrigger(final int priority) {
        super(priority); 
    }
    
    public MagicWhenLeavesPlayTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenLeavesPlay;
    }
}
