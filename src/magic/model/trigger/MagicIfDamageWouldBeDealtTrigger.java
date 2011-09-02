package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicDamage;
import magic.model.event.MagicEvent;

public abstract class MagicIfDamageWouldBeDealtTrigger extends MagicTrigger<MagicDamage> {
    public MagicIfDamageWouldBeDealtTrigger(final int priority) {
        super(MagicTriggerType.IfDamageWouldBeDealt, priority); 
	}
	
	public MagicIfDamageWouldBeDealtTrigger() {
		super(MagicTriggerType.IfDamageWouldBeDealt);
	}
}
