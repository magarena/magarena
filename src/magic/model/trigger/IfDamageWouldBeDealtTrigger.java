package magic.model.trigger;

import magic.model.MagicDamage;
import magic.model.MagicPermanent;

public abstract class IfDamageWouldBeDealtTrigger extends MagicTrigger<MagicDamage> {
    public IfDamageWouldBeDealtTrigger(final int priority) {
        super(priority);
    }

    public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
        return damage.getAmount() > 0;
    }

    public MagicTriggerType getType() {
        return MagicTriggerType.IfDamageWouldBeDealt;
    }
}
