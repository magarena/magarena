package magic.model.trigger;

import magic.model.action.MagicChangeStateAction;

public abstract class MagicWhenBecomesStateTrigger extends MagicTrigger<MagicChangeStateAction> {
    public MagicWhenBecomesStateTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenBecomesStateTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenBecomesState;
    }
}
