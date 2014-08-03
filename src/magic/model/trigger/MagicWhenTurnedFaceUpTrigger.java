package magic.model.trigger;

import magic.model.MagicPermanent;

public abstract class MagicWhenTurnedFaceUpTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenTurnedFaceUpTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenTurnedFaceUpTrigger() {}
    
    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenTurnedFaceUp;
    }
}
