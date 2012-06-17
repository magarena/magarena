package magic.model.trigger;

import magic.model.MagicCard;

public abstract class MagicWhenDrawnTrigger extends MagicTrigger<MagicCard> {
    public MagicWhenDrawnTrigger(final int priority) {
        super(priority); 
    }
    
    public MagicWhenDrawnTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenDrawn;
    }
}
