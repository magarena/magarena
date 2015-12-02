package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class BattalionTrigger extends ThisAttacksTrigger {
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent attacker) {
        return super.accept(permanent, attacker) && MagicCondition.THREE_ATTACKERS_CONDITION.accept(permanent);
    }

    public static final BattalionTrigger create(final MagicSourceEvent sourceEvent) {
        return new BattalionTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }
}
