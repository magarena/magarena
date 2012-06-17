package magic.model.trigger;

import magic.model.MagicPermanent;

public abstract class MagicWhenAttacksUnblockedTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenAttacksUnblockedTrigger(final int priority) {
        super(priority); 
    }
    
    public MagicWhenAttacksUnblockedTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenAttacksUnblocked;
    }
}
