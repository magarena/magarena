package magic.model.trigger;

import magic.model.MagicCard;

public abstract class MagicWhenOtherDrawnTrigger extends MagicTrigger<MagicCard> {
    public MagicWhenOtherDrawnTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicWhenOtherDrawnTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherDrawn;
    }
}
