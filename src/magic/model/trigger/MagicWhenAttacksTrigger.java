package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicWhenAttacksTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenAttacksTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenAttacksTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenAttacks;
    }
    
    public static final MagicWhenAttacksTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicWhenAttacksTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
