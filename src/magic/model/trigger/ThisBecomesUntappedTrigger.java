package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class ThisBecomesUntappedTrigger extends MagicWhenBecomesUntappedTrigger {
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent untapped) {
        return permanent == untapped;
    }

    public static final ThisBecomesUntappedTrigger create(final MagicSourceEvent sourceEvent) {
        return new ThisBecomesUntappedTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent untapped) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
