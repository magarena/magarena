package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicDamage;
import magic.model.stack.MagicCardOnStack;
import magic.model.event.MagicEvent;

public abstract class MagicWhenSpellIsPlayedTrigger extends MagicTrigger<MagicCardOnStack> {
    public MagicWhenSpellIsPlayedTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicWhenSpellIsPlayedTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenSpellIsPlayed;
    }
}
