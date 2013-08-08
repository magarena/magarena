package magic.model.trigger;

import magic.model.MagicPermanent;

public abstract class MagicWhenLifeIsLostTrigger extends MagicTrigger<MagicLifeChangeTriggerData> {
    public MagicWhenLifeIsLostTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenLifeIsLostTrigger() {}
    
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicLifeChangeTriggerData data) {
        return data.amount > 0;
    }

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenLifeIsLost;
    }
}
