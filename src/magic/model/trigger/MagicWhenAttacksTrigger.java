package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;

public abstract class MagicWhenAttacksTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenAttacksTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenAttacksTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenAttacks;
    }
    
    public static MagicWhenAttacksTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new MagicWhenAttacksTrigger() {
            public boolean accept(final MagicPermanent permanent, final MagicPermanent attacker) {
                return filter.accept(permanent, permanent.getController(), attacker);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
                return sourceEvent.getEvent(permanent, attacker);
            }
        };
    }
    
    public static MagicWhenAttacksTrigger createYou(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new MagicWhenAttacksTrigger() {
            public boolean accept(final MagicPermanent permanent, final MagicPermanent attacker) {
                return filter.accept(permanent, permanent.getController(), attacker) && permanent.isEnemy(attacker);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent attacker) {
                return sourceEvent.getEvent(permanent, attacker);
            }
        };
    }
}
