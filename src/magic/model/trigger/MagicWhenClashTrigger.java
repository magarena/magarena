package magic.model.trigger;

import magic.model.MagicCardDefinition;
import magic.model.stack.MagicItemOnStack;

public abstract class MagicWhenClashTrigger extends MagicTrigger<Boolean> {
    public MagicWhenClashTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenClashTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenClash;
    }
}
