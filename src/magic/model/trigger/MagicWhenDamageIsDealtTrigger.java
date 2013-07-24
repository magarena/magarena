package magic.model.trigger;

import magic.model.MagicDamage;
import magic.model.MagicPermanent;

public abstract class MagicWhenDamageIsDealtTrigger extends MagicTrigger<MagicDamage> {
    public MagicWhenDamageIsDealtTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenDamageIsDealtTrigger() {}
    
    public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
        return damage.getAmount() > 0;
    }

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenDamageIsDealt;
    }
}
