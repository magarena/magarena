package magic.model.trigger;

public abstract class MagicWhenOtherPutIntoGraveyardTrigger extends MagicTrigger<MagicGraveyardTriggerData> {
    public MagicWhenOtherPutIntoGraveyardTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenOtherPutIntoGraveyardTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherPutIntoGraveyard;
    }
}
