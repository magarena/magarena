package magic.model.trigger;

import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.MagicChangeStateAction;

public abstract class MagicWhenBecomesMonstrousTrigger extends MagicWhenBecomesStateTrigger {
    public MagicWhenBecomesMonstrousTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenBecomesMonstrousTrigger() {}
    
    public boolean accept(final MagicPermanent permanent, final MagicChangeStateAction data) {
        return data.state == MagicPermanentState.Monstrous;
    }
}
