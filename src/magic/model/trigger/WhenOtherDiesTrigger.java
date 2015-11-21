package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;

public abstract class WhenOtherDiesTrigger extends MagicTrigger<MagicPermanent> {
    public WhenOtherDiesTrigger(final int priority) {
        super(priority);
    }

    public WhenOtherDiesTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherDies;
    }

    public static final WhenOtherDiesTrigger createSelfOrAnother(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new WhenOtherDiesTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent died) {
                return permanent == died || filter.accept(permanent, permanent.getController(), died);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent died) {
                return sourceEvent.getEvent(permanent, died);
            }
        };
    }

    public static final WhenOtherDiesTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new WhenOtherDiesTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent died) {
                return filter.accept(permanent, permanent.getController(), died);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent died) {
                return sourceEvent.getEvent(permanent, died);
            }
        };
    }
}
