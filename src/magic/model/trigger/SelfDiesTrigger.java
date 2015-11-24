package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class SelfDiesTrigger extends OtherDiesTrigger {
    public static final SelfDiesTrigger create(final MagicSourceEvent sourceEvent) {
        return new SelfDiesTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent source, final MagicPermanent died) {
                return sourceEvent.getEvent(source);
            }
        };
    }

    public SelfDiesTrigger(final int priority) {
        super(priority);
    }

    public SelfDiesTrigger() {}

    @Override
    public boolean accept(final MagicPermanent source, final MagicPermanent died) {
        return source == died;
    }
}
