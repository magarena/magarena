package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.ChangeStateAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class BecomesStateTrigger extends MagicTrigger<ChangeStateAction> {
    public BecomesStateTrigger(final int priority) {
        super(priority);
    }

    public BecomesStateTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenBecomesState;
    }

    public static BecomesStateTrigger create(final MagicPermanentState state, final MagicSourceEvent sourceEvent) {
        return new BecomesStateTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final ChangeStateAction data) {
                return data.state == state;
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final ChangeStateAction data) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }

    public static BecomesStateTrigger createSelf(final MagicPermanentState state, final MagicSourceEvent sourceEvent) {
        return new BecomesStateTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final ChangeStateAction data) {
                return data.state == state && data.permanent == permanent;
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final ChangeStateAction data) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
