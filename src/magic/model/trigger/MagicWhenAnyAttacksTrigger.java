package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicWhenAnyAttacksTrigger extends MagicWhenAttacksTrigger {

    public static final MagicWhenAnyAttacksTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicWhenAnyAttacksTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
