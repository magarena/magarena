package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class ThisCycleTrigger extends MagicTrigger<MagicCard> {
    public ThisCycleTrigger(final int priority) {
        super(priority);
    }

    public ThisCycleTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenCycle;
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addTrigger(this);
    }

    public static ThisCycleTrigger create(final MagicSourceEvent sourceEvent) {
        return new ThisCycleTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCard card) {
                return sourceEvent.getTriggerEvent(card);
            }
        };
    }
}
