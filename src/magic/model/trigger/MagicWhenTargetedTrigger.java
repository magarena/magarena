package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicItemOnStack;

public abstract class MagicWhenTargetedTrigger extends MagicTrigger<MagicItemOnStack> {
    public MagicWhenTargetedTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicWhenTargetedTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenTargeted;
    }
}
