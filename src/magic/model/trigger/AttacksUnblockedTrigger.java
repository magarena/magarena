package magic.model.trigger;

import magic.model.MagicPermanent;

public abstract class AttacksUnblockedTrigger extends MagicTrigger<MagicPermanent> {
    public AttacksUnblockedTrigger(final int priority) {
        super(priority);
    }

    public AttacksUnblockedTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenAttacksUnblocked;
    }
}
