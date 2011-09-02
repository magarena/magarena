package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;

public abstract class MagicWhenBecomesTappedTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenBecomesTappedTrigger(final int priority) {
        super(MagicTriggerType.WhenBecomesTapped, priority); 
	}
	
	public MagicWhenBecomesTappedTrigger() {
		super(MagicTriggerType.WhenBecomesTapped);
	}
}
