package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicWhenSelfBecomesTappedTrigger extends MagicWhenBecomesTappedTrigger {
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent tapped) {
        return permanent == tapped;
    }
    
    public static final MagicWhenSelfBecomesTappedTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicWhenSelfBecomesTappedTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent untapped) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
