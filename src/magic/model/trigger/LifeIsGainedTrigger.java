package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class LifeIsGainedTrigger extends MagicTrigger<MagicLifeChangeTriggerData> {
    public LifeIsGainedTrigger(final int priority) {
        super(priority);
    }

    public LifeIsGainedTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicLifeChangeTriggerData data) {
        return data.amount > 0;
    }

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenLifeIsGained;
    }

    public static LifeIsGainedTrigger createYou(final MagicSourceEvent sourceEvent) {
        return new LifeIsGainedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicLifeChangeTriggerData data) {
                return super.accept(permanent, data) && permanent.isController(data.player);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicLifeChangeTriggerData data) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }
}
