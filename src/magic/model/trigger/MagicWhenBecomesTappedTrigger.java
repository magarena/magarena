package magic.model.trigger;

import magic.model.MagicPermanent;

public abstract class MagicWhenBecomesTappedTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenBecomesTappedTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenBecomesTappedTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenBecomesTapped;
    }
}
