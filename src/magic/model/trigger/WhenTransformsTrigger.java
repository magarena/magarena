package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class WhenTransformsTrigger extends MagicTrigger<MagicPermanent> {
    public WhenTransformsTrigger(final int priority) {
        super(priority);
    }

    public WhenTransformsTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenTransforms;
    }

    public static WhenTransformsTrigger createSelf(final MagicSourceEvent sourceEvent) {
        return new WhenTransformsTrigger() {
            @Override
            public boolean accept(final MagicPermanent source, final MagicPermanent permanent) {
                return source == permanent;
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent source, final MagicPermanent permanent) {
                return sourceEvent.getTriggerEvent(source, permanent);
            }
        };
    }
}
