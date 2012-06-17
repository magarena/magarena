package magic.model.trigger;

import magic.model.stack.MagicCardOnStack;

public abstract class MagicWhenSpellIsCastTrigger extends MagicTrigger<MagicCardOnStack> {
    public MagicWhenSpellIsCastTrigger(final int priority) {
        super(priority); 
    }
    
    public MagicWhenSpellIsCastTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenSpellIsCast;
    }
}
