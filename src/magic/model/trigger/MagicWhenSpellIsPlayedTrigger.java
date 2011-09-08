package magic.model.trigger;

import magic.model.stack.MagicCardOnStack;

public abstract class MagicWhenSpellIsPlayedTrigger extends MagicTrigger<MagicCardOnStack> {
    public MagicWhenSpellIsPlayedTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicWhenSpellIsPlayedTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenSpellIsPlayed;
    }
}
