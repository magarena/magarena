package magic.model.trigger;

import magic.model.MagicDamage;

public abstract class MagicIfDamageWouldBeDealtTrigger extends MagicTrigger<MagicDamage> {
    public MagicIfDamageWouldBeDealtTrigger(final int priority) {
        super(priority); 
    }
    
    public MagicIfDamageWouldBeDealtTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.IfDamageWouldBeDealt;
    }
}
