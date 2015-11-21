package magic.model.trigger;

import magic.model.action.LoseGameAction;

public abstract class IfPlayerWouldLoseTrigger extends MagicTrigger<LoseGameAction> {
    public IfPlayerWouldLoseTrigger(final int priority) {
        super(priority);
    }

    public IfPlayerWouldLoseTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.IfPlayerWouldLose;
    }
}
