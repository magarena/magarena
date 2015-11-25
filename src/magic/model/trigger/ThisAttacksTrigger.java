package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class ThisAttacksTrigger extends MagicWhenAttacksTrigger {
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent attacker) {
        return permanent == attacker;
    }

    public static final ThisAttacksTrigger create(final MagicSourceEvent sourceEvent) {
        return new ThisAttacksTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
