package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicCard;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class OtherCycleTrigger extends MagicTrigger<MagicCard> {
    public OtherCycleTrigger(final int priority) {
        super(priority);
    }

    public OtherCycleTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherCycle;
    }

    public static OtherCycleTrigger create(final MagicSourceEvent sourceEvent) {
        return new OtherCycleTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCard card) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }
}
