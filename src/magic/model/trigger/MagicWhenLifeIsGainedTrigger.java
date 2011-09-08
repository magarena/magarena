
package magic.model.trigger;

import magic.model.MagicPlayer;

public abstract class MagicWhenLifeIsGainedTrigger extends MagicTrigger<MagicPlayer> {
    public MagicWhenLifeIsGainedTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicWhenLifeIsGainedTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenLifeIsGained;
    }
}
