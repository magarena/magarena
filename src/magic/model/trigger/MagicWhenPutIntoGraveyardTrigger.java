package magic.model.trigger;


public abstract class MagicWhenPutIntoGraveyardTrigger extends MagicTrigger<MagicGraveyardTriggerData> {
    public MagicWhenPutIntoGraveyardTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicWhenPutIntoGraveyardTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenPutIntoGraveyard;
    }
}
