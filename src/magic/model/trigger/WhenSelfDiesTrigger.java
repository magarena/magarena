package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class WhenSelfDiesTrigger extends WhenOtherDiesTrigger {
    public static final WhenSelfDiesTrigger create(final MagicSourceEvent sourceEvent) {
        return new WhenSelfDiesTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent source, final MagicPermanent died) {
                return sourceEvent.getEvent(source);
            }
        };
    }

    public WhenSelfDiesTrigger(final int priority) {
        super(priority);
    }

    public WhenSelfDiesTrigger() {}

    @Override
    public boolean accept(final MagicPermanent source, final MagicPermanent died) {
        return source == died;
    }
}
