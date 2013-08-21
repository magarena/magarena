package magic.model.trigger;

import magic.model.action.MagicLoseGameAction;
import magic.model.MagicPlayer;

public abstract class MagicIfPlayerWouldLoseTrigger extends MagicTrigger<MagicLoseGameAction> {
    public MagicIfPlayerWouldLoseTrigger(final int priority) {
        super(priority);
    }

    public MagicIfPlayerWouldLoseTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.IfPlayerWouldLose;
    }
}
