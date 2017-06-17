package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;

public abstract class AttacksUnblockedTrigger extends MagicTrigger<MagicPermanent> {
    public AttacksUnblockedTrigger(final int priority) {
        super(priority);
    }

    public AttacksUnblockedTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenAttacksUnblocked;
    }

    public static AttacksUnblockedTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new AttacksUnblockedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent attacker) {
                return filter.accept(permanent, permanent.getController(), attacker);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
                return sourceEvent.getTriggerEvent(permanent, attacker);
            }
        };
    }
}
