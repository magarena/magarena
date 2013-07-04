package magic.model.trigger;

import magic.model.MagicPermanent;

public abstract class MagicWhenLifeIsGainedTrigger extends MagicTrigger<MagicLifeChangeTriggerData> {
    public MagicWhenLifeIsGainedTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenLifeIsGainedTrigger() {}
    
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicLifeChangeTriggerData data) {
        return data.amount > 0;
    }

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenLifeIsGained;
    }
}
