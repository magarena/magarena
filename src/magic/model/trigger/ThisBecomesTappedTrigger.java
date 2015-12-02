package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class ThisBecomesTappedTrigger extends BecomesTappedTrigger {
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent tapped) {
        return permanent == tapped;
    }

    public static final ThisBecomesTappedTrigger create(final MagicSourceEvent sourceEvent) {
        return new ThisBecomesTappedTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent untapped) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }
}
