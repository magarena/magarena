package magic.model.trigger;

import magic.model.MagicPlayer;
import magic.model.MagicCardDefinition;

public abstract class MagicWhenComesIntoPlayTrigger extends MagicTrigger<MagicPlayer> {
    public MagicWhenComesIntoPlayTrigger(final int priority) {
        super(priority); 
    }
    
    public MagicWhenComesIntoPlayTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenComesIntoPlay;
    }
    
    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addTrigger(this);
    }
}
