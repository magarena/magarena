package magic.model.trigger;

import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MoveCardAction;

public abstract class OtherPutIntoGraveyardTrigger extends MagicTrigger<MoveCardAction> {
    public OtherPutIntoGraveyardTrigger(final int priority) {
        super(priority);
    }

    public OtherPutIntoGraveyardTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherPutIntoGraveyard;
    }

    @Override
    public boolean accept(final MagicPermanent permanent, final MoveCardAction act) {
        return act.to(MagicLocationType.Graveyard);
    }
}
