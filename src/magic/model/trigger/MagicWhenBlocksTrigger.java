package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;

public abstract class MagicWhenBlocksTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenBlocksTrigger(final int priority) {
        super(MagicTriggerType.WhenBlocks, priority); 
	}
	
	public MagicWhenBlocksTrigger() {
		super(MagicTriggerType.WhenBlocks);
	}
}
