package magic.model.trigger;

import magic.model.action.MoveCardAction;

public abstract class MagicWouldBeMovedTrigger extends MagicTrigger<MoveCardAction> {
    public MagicWouldBeMovedTrigger(final int priority) {
        super(priority);
    }

    public MagicWouldBeMovedTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WouldBeMoved;
    }
}
