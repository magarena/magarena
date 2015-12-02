package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;

public abstract class BecomesTappedTrigger extends MagicTrigger<MagicPermanent> {
    public BecomesTappedTrigger(final int priority) {
        super(priority);
    }

    public BecomesTappedTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenBecomesTapped;
    }

    public static final BecomesTappedTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new ThisBecomesTappedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent tapped) {
                return filter.accept(permanent, permanent.getController(), tapped);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent tapped) {
                return sourceEvent.getTriggerEvent(permanent, tapped);
            }
        };
    }
}
