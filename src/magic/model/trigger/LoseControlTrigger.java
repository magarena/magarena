package magic.model.trigger;

import magic.model.MagicPermanent;

public abstract class LoseControlTrigger extends MagicTrigger<MagicPermanent> {
    public LoseControlTrigger(final int priority) {
        super(priority);
    }

    public LoseControlTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenLoseControl;
    }
}
