package magic.model.event;

import magic.model.MagicSource;
import magic.model.condition.MagicCondition;

public class MagicConditionEvent extends MagicEvent {

    private final MagicCondition cond;

    public MagicConditionEvent(final MagicSource source, final MagicCondition condition) {
        super(
            source,
            MagicEventAction.NONE,
            ""
        );
        cond = condition;
    }

    @Override
    public boolean isSatisfied() {
        return cond.accept(getSource()) && super.isSatisfied();
    }
}
