package magic.model.trigger;

import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicMoveCardAction;

public abstract class MagicWhenOtherPutIntoGraveyardTrigger extends MagicTrigger<MagicMoveCardAction> {
    public MagicWhenOtherPutIntoGraveyardTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenOtherPutIntoGraveyardTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherPutIntoGraveyard;
    }
    
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicMoveCardAction act) {
        return act.getToLocation() == MagicLocationType.Graveyard;
    }
}
