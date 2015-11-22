package magic.model.trigger;

import magic.model.MagicPermanent;

public abstract class LifeIsLostTrigger extends MagicTrigger<MagicLifeChangeTriggerData> {
    public LifeIsLostTrigger(final int priority) {
        super(priority);
    }

    public LifeIsLostTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicLifeChangeTriggerData data) {
        return data.amount > 0;
    }

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenLifeIsLost;
    }
}
