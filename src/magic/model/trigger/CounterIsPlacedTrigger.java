package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class CounterIsPlacedTrigger extends MagicTrigger<MagicCounterChangeTriggerData> {

    public CounterIsPlacedTrigger(final int priority) {
        super(priority);
    }

    public CounterIsPlacedTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
        return data.amount > 0;
    }

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenCounterIsPlaced;
    }

    public static CounterIsPlacedTrigger createOneOrMoreSelf(MagicCounterType counterType, final MagicSourceEvent sourceEvent) {
        return new CounterIsPlacedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
                return super.accept(permanent, data) && permanent.getId() == data.obj.getId() && data.counterType == counterType;
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }

    public static CounterIsPlacedTrigger createYou(final MagicSourceEvent sourceEvent) {
        return new CounterIsPlacedTrigger() {
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
