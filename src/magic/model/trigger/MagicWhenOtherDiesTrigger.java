package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;

public abstract class MagicWhenOtherDiesTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenOtherDiesTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenOtherDiesTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherDies;
    }
    
    public static final MagicWhenOtherDiesTrigger createSelfOrAnother(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new MagicWhenOtherDiesTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent died) {
                return permanent == died || filter.accept(permanent, permanent.getController(), died);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent died) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
    
    public static final MagicWhenOtherDiesTrigger createAnother(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new MagicWhenOtherDiesTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent died) {
                return permanent != died && filter.accept(permanent, permanent.getController(), died);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent died) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
    
    public static final MagicWhenOtherDiesTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new MagicWhenOtherDiesTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent died) {
                return filter.accept(permanent, permanent.getController(), died);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent died) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
