package magic.model.event;

import magic.model.MagicSource;
import magic.model.condition.MagicCondition;

public class MagicConditionEvent extends MagicEvent {

    private final MagicCondition[] conds;

    public MagicConditionEvent(final MagicSource source, final MagicCondition condition) {
        super(
            source,
            MagicEventAction.NONE,
            ""
        );
        conds = new MagicCondition[]{
            condition
        };
    }

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}
