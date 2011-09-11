package magic.model.trigger;

import magic.model.MagicPlayer;

public abstract class MagicWhenLifeIsDamagedTrigger extends MagicTrigger<MagicPlayer> {
    public MagicWhenLifeIsDamagedTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicWhenLifeIsDamagedTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenLifeIsDamaged;
    }
}
