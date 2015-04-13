package magic.model.trigger;

import magic.model.action.ChangeLifeAction;

public abstract class MagicIfLifeWouldChangeTrigger extends MagicTrigger<ChangeLifeAction> {
    public MagicIfLifeWouldChangeTrigger(final int priority) {
        super(priority);
    }

    public MagicIfLifeWouldChangeTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.IfLifeWouldChange;
    }
}
