package magic.model.trigger;

import magic.model.MagicCard;

public abstract class MagicWhenDiscardedTrigger extends MagicTrigger<MagicCard> {
    public MagicWhenDiscardedTrigger(final int priority) {
        super(priority); 
    }
    
    public MagicWhenDiscardedTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenDiscarded;
    }
}
