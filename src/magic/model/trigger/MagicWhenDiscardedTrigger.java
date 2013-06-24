package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicLocationType;
import magic.model.event.MagicEvent;

public abstract class MagicWhenDiscardedTrigger extends MagicWhenOtherPutIntoGraveyardTrigger {
    public MagicWhenDiscardedTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenDiscardedTrigger() {}

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
        return (triggerData.fromLocation == MagicLocationType.OwnersHand) ?
            getEvent(permanent, triggerData.card) : MagicEvent.NONE;
    }

    protected abstract MagicEvent getEvent(final MagicPermanent source, final MagicCard card);
}
