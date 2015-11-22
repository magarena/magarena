package magic.model.trigger;

import magic.model.MagicDamage;
import magic.model.MagicPermanent;

public abstract class MagicWhenSelfDamagePlayerTrigger extends DamageIsDealtTrigger {
    public MagicWhenSelfDamagePlayerTrigger() {}

    public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
        return super.accept(permanent, damage) &&
               damage.isSource(permanent) &&
               damage.isTargetPlayer();
    }
}
