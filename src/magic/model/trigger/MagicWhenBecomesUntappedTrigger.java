package magic.model.trigger;

import magic.model.MagicPermanent;

public abstract class MagicWhenBecomesUntappedTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenBecomesUntappedTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenBecomesUntappedTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenBecomesUntapped;
    }
}
