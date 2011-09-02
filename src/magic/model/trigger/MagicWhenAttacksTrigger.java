package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;

public abstract class MagicWhenAttacksTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenAttacksTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicWhenAttacksTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenAttacks;
    }
}
