package magic.model.trigger;

import magic.model.MagicCardDefinition;

public abstract class MagicWhenPutIntoGraveyardTrigger extends MagicTrigger<MagicGraveyardTriggerData> {
    public MagicWhenPutIntoGraveyardTrigger(final int priority) {
        super(priority); 
    }
    
    public MagicWhenPutIntoGraveyardTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenPutIntoGraveyard;
    }
    
    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addTrigger(this);
    }
}
