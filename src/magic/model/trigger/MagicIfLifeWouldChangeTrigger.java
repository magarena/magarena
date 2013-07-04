package magic.model.trigger;

import magic.model.action.MagicChangeLifeAction;

public abstract class MagicIfLifeWouldChangeTrigger extends MagicTrigger<MagicChangeLifeAction> {
    public MagicIfLifeWouldChangeTrigger(final int priority) {
        super(priority);
    }

    public MagicIfLifeWouldChangeTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.IfLifeWouldChange;
    }
}
