package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicWhenSelfBecomesBlockedTrigger extends BecomesBlockedTrigger {
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent blocked) {
        return permanent == blocked;
    }

    public static final MagicWhenSelfBecomesBlockedTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicWhenSelfBecomesBlockedTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent blocked) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
