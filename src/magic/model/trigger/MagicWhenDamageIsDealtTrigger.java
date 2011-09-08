package magic.model.trigger;

import magic.model.MagicDamage;

public abstract class MagicWhenDamageIsDealtTrigger extends MagicTrigger<MagicDamage> {
    public MagicWhenDamageIsDealtTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicWhenDamageIsDealtTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenDamageIsDealt;
    }
}
