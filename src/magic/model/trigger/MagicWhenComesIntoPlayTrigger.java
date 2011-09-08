package magic.model.trigger;

import magic.model.MagicPlayer;

public abstract class MagicWhenComesIntoPlayTrigger extends MagicTrigger<MagicPlayer> {
    public MagicWhenComesIntoPlayTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicWhenComesIntoPlayTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenComesIntoPlay;
    }
}
