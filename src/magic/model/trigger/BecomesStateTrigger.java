package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.ChangeStateAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;

public abstract class BecomesStateTrigger extends MagicTrigger<ChangeStateAction> {
    public BecomesStateTrigger(final int priority) {
        super(priority);
    }

    public BecomesStateTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenBecomesState;
    }

    public static BecomesStateTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicPermanentState state, final MagicSourceEvent sourceEvent) {
        return new BecomesStateTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final ChangeStateAction data) {
                return filter.accept(permanent, permanent.getController(), data.permanent) && data.state == state;
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final ChangeStateAction data) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }

    public static BecomesStateTrigger createSelf(final MagicPermanentState state, final MagicSourceEvent sourceEvent) {
        return create(MagicTargetFilterFactory.SN, state, sourceEvent);
    }
}
