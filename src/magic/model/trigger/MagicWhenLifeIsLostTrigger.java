package magic.model.trigger;

public abstract class MagicWhenLifeIsLostTrigger extends MagicTrigger<MagicLifeChangeTriggerData> {
    public MagicWhenLifeIsLostTrigger(final int priority) {
        super(priority); 
    }
    
    public MagicWhenLifeIsLostTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenLifeIsLost;
    }
}
