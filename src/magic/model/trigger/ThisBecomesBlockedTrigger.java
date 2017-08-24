package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.target.MagicTargetFilter;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class ThisBecomesBlockedTrigger extends BecomesBlockedTrigger {
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent blocked) {
        return permanent == blocked;
    }

    public static final ThisBecomesBlockedTrigger create(final MagicSourceEvent sourceEvent) {
        return new ThisBecomesBlockedTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent blocked) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }

    public static final ThisBecomesBlockedTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new ThisBecomesBlockedTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent blocked) {
                final MagicPermanentList plist = permanent.getBlockingCreatures();
                final boolean matched = plist.stream().anyMatch(it -> filter.accept(permanent, permanent.getController(), it));
                return matched ? sourceEvent.getTriggerEvent(permanent) : MagicEvent.NONE;
            }
        };
    }
}
