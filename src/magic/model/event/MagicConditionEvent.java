package magic.model.event;

import magic.model.MagicSource;
import magic.model.MagicCopyMap;
import magic.model.condition.MagicCondition;

public class MagicConditionEvent extends MagicEvent {

    private final MagicCondition cond;

    public MagicConditionEvent(final MagicSource source, final MagicCondition condition, final MagicEventAction eventAction) {
        super(
            source,
            eventAction,
            ""
        );
        cond = condition;
    }

    @Override
    public boolean isSatisfied() {
        return cond.accept(getSource()) && super.isSatisfied();
    }

    @Override
    public MagicEvent copy(final MagicCopyMap copyMap) {
        return new MagicConditionEvent(
            copyMap.copy(getSource()),
            cond,
            getEventAction()
        );
    }
}
