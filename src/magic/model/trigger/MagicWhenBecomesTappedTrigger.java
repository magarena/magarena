package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;

public abstract class MagicWhenBecomesTappedTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenBecomesTappedTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenBecomesTappedTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenBecomesTapped;
    }
    
    public static final MagicWhenBecomesTappedTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new MagicWhenSelfBecomesTappedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent tapped) {
                return filter.accept(permanent, permanent.getController(), tapped);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent tapped) {
                return sourceEvent.getEvent(permanent, tapped);
            }
        };
    }
}
