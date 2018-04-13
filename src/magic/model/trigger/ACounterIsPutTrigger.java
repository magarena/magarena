package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;

public abstract class ACounterIsPutTrigger extends MagicTrigger<MagicCounterChangeTriggerData> {

    public ACounterIsPutTrigger(final int priority) {
        super(priority);
    }

    public ACounterIsPutTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
        return data.amount > 0;
    }

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenACounterIsChanged;
    }

    public static ACounterIsPutTrigger create(final MagicTargetFilter<MagicPermanent> filter, MagicCounterType counterType, final MagicSourceEvent sourceEvent) {
        return new ACounterIsPutTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
                return super.accept(permanent, data) &&
                    data.obj.isPermanent() &&
                    filter.accept(permanent, permanent.getController(), (MagicPermanent)data.obj) &&
                    data.counterType == counterType;
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }

    public static ACounterIsPutTrigger createYou(final MagicTargetFilter<MagicPermanent> filter, MagicCounterType counterType, final MagicSourceEvent sourceEvent) {
        return new ACounterIsPutTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
                return super.accept(permanent, data) &&
                    permanent.isController(data.player) &&
                    data.obj.isPermanent() &&
                    filter.accept(permanent, permanent.getController(), (MagicPermanent)data.obj) &&
                    data.counterType == counterType;
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCounterChangeTriggerData data) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }

}
