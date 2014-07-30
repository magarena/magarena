package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.MagicChangeStateAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicWhenBecomesStateTrigger extends MagicTrigger<MagicChangeStateAction> {
    public MagicWhenBecomesStateTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenBecomesStateTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenBecomesState;
    }
    
    public static MagicWhenBecomesStateTrigger create(final MagicPermanentState state, final MagicSourceEvent sourceEvent) {
        return new MagicWhenBecomesStateTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicChangeStateAction data) {
                return data.state == state;
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicChangeStateAction data) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
