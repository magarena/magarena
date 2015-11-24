package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class SelfBecomesBlockedTrigger extends BecomesBlockedTrigger {
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent blocked) {
        return permanent == blocked;
    }

    public static final SelfBecomesBlockedTrigger create(final MagicSourceEvent sourceEvent) {
        return new SelfBecomesBlockedTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent blocked) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
