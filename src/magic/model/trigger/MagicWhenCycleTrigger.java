package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicLocationType;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.action.MagicChangeCardDestinationAction;

public abstract class MagicWhenCycleTrigger extends MagicTrigger<MagicCard> {
    public MagicWhenCycleTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenCycleTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenCycle;
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addTrigger(this);
    }
    
    public static MagicWhenCycleTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicWhenCycleTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCard card) {
                return sourceEvent.getEvent(card);
            }
        };
    }
}
