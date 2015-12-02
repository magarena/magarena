package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;

public abstract class BecomesUntappedTrigger extends MagicTrigger<MagicPermanent> {
    public BecomesUntappedTrigger(final int priority) {
        super(priority);
    }

    public BecomesUntappedTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenBecomesUntapped;
    }

    public static final BecomesUntappedTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new ThisBecomesUntappedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent untapped) {
                return filter.accept(permanent, permanent.getController(), untapped);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent untapped) {
                return sourceEvent.getTriggerEvent(permanent, untapped);
            }
        };
    }
}
