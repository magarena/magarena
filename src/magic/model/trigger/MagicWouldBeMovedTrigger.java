package magic.model.trigger;

import magic.model.action.MagicMoveCardAction;

public abstract class MagicWouldBeMovedTrigger extends MagicTrigger<MagicMoveCardAction> {
    public MagicWouldBeMovedTrigger(final int priority) {
        super(priority);
    }

    public MagicWouldBeMovedTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WouldBeMoved;
    }
}
