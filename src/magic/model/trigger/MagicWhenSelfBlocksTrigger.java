package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicWhenSelfBlocksTrigger extends MagicWhenBlocksTrigger {
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent blocker) {
        return permanent == blocker;
    }
    
    public static final MagicWhenSelfBlocksTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicWhenSelfBlocksTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent blocker) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
