package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;

public abstract class ThisBlocksTrigger extends MagicWhenBlocksTrigger {
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent blocker) {
        return permanent == blocker;
    }

    public static final ThisBlocksTrigger create(final MagicSourceEvent sourceEvent) {
        return new ThisBlocksTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent blocker) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }

    public static final ThisBlocksTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new ThisBlocksTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent blocker) {
                return super.accept(permanent, blocker) &&
                       filter.accept(permanent, permanent.getController(), permanent.getBlockedCreature());
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent blocker) {
                return sourceEvent.getEvent(permanent, permanent.getBlockedCreature());
            }
        };
    }
}
