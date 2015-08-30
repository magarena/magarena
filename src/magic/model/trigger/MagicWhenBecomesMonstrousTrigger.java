package magic.model.trigger;

import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.ChangeStateAction;

public abstract class MagicWhenBecomesMonstrousTrigger extends MagicWhenBecomesStateTrigger {
    public MagicWhenBecomesMonstrousTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenBecomesMonstrousTrigger() {}
    
    public boolean accept(final MagicPermanent permanent, final ChangeStateAction data) {
        return data.state == MagicPermanentState.Monstrous;
    }
}
