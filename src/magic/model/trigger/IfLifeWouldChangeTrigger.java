package magic.model.trigger;

import magic.model.action.ChangeLifeAction;

public abstract class IfLifeWouldChangeTrigger extends MagicTrigger<ChangeLifeAction> {
    public IfLifeWouldChangeTrigger(final int priority) {
        super(priority);
    }

    public IfLifeWouldChangeTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.IfLifeWouldChange;
    }
}
