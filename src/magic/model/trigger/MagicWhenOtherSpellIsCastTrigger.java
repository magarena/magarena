package magic.model.trigger;

import magic.model.stack.MagicCardOnStack;

public abstract class MagicWhenOtherSpellIsCastTrigger extends MagicTrigger<MagicCardOnStack> {
    public MagicWhenOtherSpellIsCastTrigger(final int priority) {
        super(priority); 
    }
    
    public MagicWhenOtherSpellIsCastTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherSpellIsCast;
    }
}
