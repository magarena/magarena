package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class CounterIsRemovedTrigger extends MagicTrigger<MagicCounterChangeTriggerData> {

    public CounterIsRemovedTrigger(final int priority) {
        super(priority);
    }

    public CounterIsRemovedTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
        return data.amount < 0;
    }

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenCounterIsRemoved;
    }

    public static CounterIsRemovedTrigger createSelf(final MagicSourceEvent sourceEvent) {
        return new CounterIsRemovedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
                return super.accept(permanent, data) && permanent.getId() == data.obj.getId();
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }

    public static CounterIsRemovedTrigger createYou(final MagicSourceEvent sourceEvent) {
        return new CounterIsRemovedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
                return super.accept(permanent, data) && permanent.getController().getId() == data.obj.getId();
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }
}
