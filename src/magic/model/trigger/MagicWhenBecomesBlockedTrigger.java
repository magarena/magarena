package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicSourceEvent;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;

public abstract class MagicWhenBecomesBlockedTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenBecomesBlockedTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenBecomesBlockedTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenBecomesBlocked;
    }
    
    public static final MagicWhenBecomesBlockedTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new MagicWhenBecomesBlockedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent blocked) {
                return filter.accept(permanent, permanent.getController(), blocked);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent blocked) {
                return sourceEvent.getEvent(permanent, blocked);
            }
        };
    }
}
