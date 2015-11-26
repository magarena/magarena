package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;

public abstract class ThisBecomesBlockedByTrigger extends BlocksTrigger {
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent blocker) {
        return permanent == blocker.getBlockedCreature();
    }

    public static final ThisBecomesBlockedByTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new ThisBecomesBlockedByTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent blocker) {
                return super.accept(permanent, blocker) &&
                       filter.accept(permanent, permanent.getController(), blocker);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent blocker) {
                return sourceEvent.getEvent(permanent, blocker);
            }
        };
    }
}
