package magic.model.trigger;

import magic.model.action.ChangeCountersAction;

public abstract class IfCounterWouldChangeTrigger extends MagicTrigger<ChangeCountersAction> {
    public IfCounterWouldChangeTrigger(final int priority) {
        super(priority);
    }

    public IfCounterWouldChangeTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.IfCounterWouldChange;
    }
}
