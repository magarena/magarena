package magic.model.trigger;

import magic.model.MagicPermanent;
import magic.model.MagicGame;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.condition.MagicCondition;

public abstract class MagicBattalionTrigger extends MagicWhenAttacksTrigger {
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent attacker) {
        return permanent == attacker && MagicCondition.THREE_ATTACKERS_CONDITION.accept(permanent);
    }

    public static final MagicBattalionTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicBattalionTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
