package magic.model.trigger;

import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.action.ChangeStateAction;

public abstract class MonstrousTrigger extends BecomesStateTrigger {
    public MonstrousTrigger(final int priority) {
        super(priority);
    }

    public MonstrousTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final ChangeStateAction data) {
        return data.state == MagicPermanentState.Monstrous;
    }
}
