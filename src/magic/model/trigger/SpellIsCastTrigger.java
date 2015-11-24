package magic.model.trigger;

import magic.model.MagicCardDefinition;
import magic.model.stack.MagicCardOnStack;

public abstract class SpellIsCastTrigger extends MagicTrigger<MagicCardOnStack> {
    public SpellIsCastTrigger(final int priority) {
        super(priority);
    }

    public SpellIsCastTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenSpellIsCast;
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addTrigger(this);
    }
}
