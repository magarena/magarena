package magic.model.trigger;

import magic.model.MagicCardDefinition;
import magic.model.stack.MagicItemOnStack;

public abstract class MagicWhenClashTrigger extends MagicTrigger<MagicItemOnStack> {
    public MagicWhenClashTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenClashTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenClash;
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addTrigger(this);
    }
    
    @Override
    public boolean usesStack() {
        return true;
    }
}
