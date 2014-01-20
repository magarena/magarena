package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicWhenSelfBecomesUntappedTrigger extends MagicWhenBecomesUntappedTrigger {
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent untapped) {
        return permanent == untapped;
    }
    
    public static final MagicWhenSelfBecomesUntappedTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicWhenSelfBecomesUntappedTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent untapped) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
