package magic.model.trigger;

import magic.model.MagicPermanent;

public abstract class MagicWhenOtherPutIntoGraveyardFromPlayTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenOtherPutIntoGraveyardFromPlayTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay;
    }
}
