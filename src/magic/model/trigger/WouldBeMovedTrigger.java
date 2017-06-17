package magic.model.trigger;

import magic.model.action.MoveCardAction;

public abstract class WouldBeMovedTrigger extends MagicTrigger<MoveCardAction> {
    public WouldBeMovedTrigger(final int priority) {
        super(priority);
    }

    public WouldBeMovedTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WouldBeMoved;
    }
}
