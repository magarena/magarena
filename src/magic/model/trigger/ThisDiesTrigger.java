package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class ThisDiesTrigger extends OtherDiesTrigger {
    public static final ThisDiesTrigger create(final MagicSourceEvent sourceEvent) {
        return new ThisDiesTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent source, final MagicPermanent died) {
                return sourceEvent.getTriggerEvent(source);
            }
        };
    }

    public ThisDiesTrigger(final int priority) {
        super(priority);
    }

    public ThisDiesTrigger() {}

    @Override
    public boolean accept(final MagicPermanent source, final MagicPermanent died) {
        return source == died;
    }
}
