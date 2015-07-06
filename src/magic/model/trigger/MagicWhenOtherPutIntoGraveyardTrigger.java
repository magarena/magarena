package magic.model.trigger;

import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MoveCardAction;

public abstract class MagicWhenOtherPutIntoGraveyardTrigger extends MagicTrigger<MoveCardAction> {
    public MagicWhenOtherPutIntoGraveyardTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenOtherPutIntoGraveyardTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherPutIntoGraveyard;
    }
    
    @Override
    public boolean accept(final MagicPermanent permanent, final MoveCardAction act) {
        return act.to(MagicLocationType.Graveyard);
    }
}
