package magic.model.trigger;

import magic.model.MagicPlayer;

public abstract class MagicAtUpkeepTrigger extends MagicTrigger<MagicPlayer> {
    public MagicAtUpkeepTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicAtUpkeepTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.AtUpkeep;
    }
}
