package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;

public abstract class MagicWhenBecomesUntappedTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenBecomesUntappedTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenBecomesUntappedTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenBecomesUntapped;
    }
    
    public static final MagicWhenBecomesUntappedTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new MagicWhenSelfBecomesUntappedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent untapped) {
                return filter.accept(permanent, permanent.getController(), untapped);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent untapped) {
                return sourceEvent.getEvent(permanent, untapped);
            }
        };
    }
}
