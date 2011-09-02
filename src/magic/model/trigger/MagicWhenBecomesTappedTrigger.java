package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;

public abstract class MagicWhenBecomesTappedTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenBecomesTappedTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicWhenBecomesTappedTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenBecomesTapped;
    }
}
