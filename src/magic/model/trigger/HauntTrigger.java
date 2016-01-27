package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicHauntEvent;
import magic.model.event.MagicSourceEvent;

public abstract class HauntTrigger extends ThisDiesTrigger {

    private HauntTrigger() {}

    public static final HauntTrigger create(final MagicSourceEvent sourceEvent) {
        return new HauntTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
                return new MagicHauntEvent(permanent, sourceEvent);
            }
        };
    }
}
