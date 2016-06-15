package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicLocationType;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.action.RemoveFromPlayAction;

public abstract class SacrificeTrigger extends MagicTrigger<RemoveFromPlayAction> {
    public SacrificeTrigger(final int priority) {
        super(priority);
    }

    public SacrificeTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenSacrifice;
    }

    public static final SacrificeTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new SacrificeTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final RemoveFromPlayAction act) {
                return filter.accept(permanent, permanent.getController(), act.getPermanent());
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final RemoveFromPlayAction act) {
                return sourceEvent.getTriggerEvent(permanent, act.getPermanent());
            }
        };
    }
}
