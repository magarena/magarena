package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class CounterIsAddedTrigger extends MagicTrigger<MagicCounterChangeTriggerData> {

    public CounterIsAddedTrigger(final int priority) {
        super(priority);
    }

    public CounterIsAddedTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
        return data.amount > 0;
    }

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenCounterIsAdded;
    }

    public static CounterIsAddedTrigger createSelf(final MagicSourceEvent sourceEvent) {
        return new CounterIsAddedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
                return super.accept(permanent, data) && permanent.getId() == data.object.getId();
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }

    public static CounterIsAddedTrigger createYou(final MagicSourceEvent sourceEvent) {
        return new CounterIsAddedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
                return super.accept(permanent, data) && permanent.getController().getId() == data.object.getId();
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }
}
