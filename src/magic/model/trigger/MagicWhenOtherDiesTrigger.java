package magic.model.trigger;

import magic.model.MagicPermanent;

public abstract class MagicWhenOtherDiesTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenOtherDiesTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenOtherDiesTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherDies;
    }
}
