package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;

public abstract class MagicWhenBlocksTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenBlocksTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenBlocksTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenBlocks;
    }
    
    public static MagicWhenBlocksTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new MagicWhenBlocksTrigger() {
            public boolean accept(final MagicPermanent permanent, final MagicPermanent blocker) {
                return filter.accept(permanent, permanent.getController(), blocker);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent blocker) {
                return sourceEvent.getEvent(permanent, blocker);
            }
        };
    }
}
