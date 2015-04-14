package magic.model.trigger;

import magic.model.action.LoseGameAction;

public abstract class MagicIfPlayerWouldLoseTrigger extends MagicTrigger<LoseGameAction> {
    public MagicIfPlayerWouldLoseTrigger(final int priority) {
        super(priority);
    }

    public MagicIfPlayerWouldLoseTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.IfPlayerWouldLose;
    }
}
