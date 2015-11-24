package magic.model.trigger;

import magic.model.MagicCardDefinition;
import magic.model.stack.MagicCardOnStack;

public abstract class ThisSpellIsCastTrigger extends MagicTrigger<MagicCardOnStack> {
    public ThisSpellIsCastTrigger(final int priority) {
        super(priority);
    }

    public ThisSpellIsCastTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenSpellIsCast;
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addTrigger(this);
    }
}
