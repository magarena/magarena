package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicDamage;
import magic.model.MagicCard;
import magic.model.event.MagicEvent;

public abstract class MagicWhenDiscardedTrigger extends MagicTrigger<MagicCard> {
    public MagicWhenDiscardedTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicWhenDiscardedTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenDiscarded;
    }
}
