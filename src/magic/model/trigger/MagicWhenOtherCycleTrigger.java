package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicCard;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicWhenOtherCycleTrigger extends MagicTrigger<MagicCard> {
    public MagicWhenOtherCycleTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenOtherCycleTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherCycle;
    }
    
    public static MagicWhenOtherCycleTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicWhenOtherCycleTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCard card) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
