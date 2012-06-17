package magic.model.trigger;

import magic.model.MagicPermanent;

public abstract class MagicWhenOtherComesIntoPlayTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenOtherComesIntoPlayTrigger(final int priority) {
        super(priority); 
    }
    
    public MagicWhenOtherComesIntoPlayTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherComesIntoPlay;
    }
}
