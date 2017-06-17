package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;

public abstract class OtherDiesTrigger extends MagicTrigger<MagicPermanent> {
    public OtherDiesTrigger(final int priority) {
        super(priority);
    }

    public OtherDiesTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherDies;
    }

    public static final OtherDiesTrigger createSelfOrAnother(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new OtherDiesTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent died) {
                return permanent == died || filter.accept(permanent, permanent.getController(), died);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent died) {
                return sourceEvent.getTriggerEvent(permanent, died);
            }
        };
    }

    public static final OtherDiesTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new OtherDiesTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent died) {
                return filter.accept(permanent, permanent.getController(), died);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent died) {
                return sourceEvent.getTriggerEvent(permanent, died);
            }
        };
    }
}
