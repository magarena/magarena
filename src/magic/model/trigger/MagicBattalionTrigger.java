package magic.model.trigger;

import magic.model.MagicPermanent;
import magic.model.condition.MagicCondition;

public abstract class MagicBattalionTrigger extends MagicWhenAttacksTrigger {
    public boolean accept(final MagicPermanent permanent, final MagicPermanent attacker) {
        return permanent == attacker && MagicCondition.THREE_ATTACKERS_CONDITION.accept(permanent);
    }
}
