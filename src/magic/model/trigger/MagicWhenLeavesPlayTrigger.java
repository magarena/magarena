package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicLocationType;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.action.MagicRemoveFromPlayAction;

public abstract class MagicWhenLeavesPlayTrigger extends MagicTrigger<MagicRemoveFromPlayAction> {
    public MagicWhenLeavesPlayTrigger(final int priority) {
        super(priority);
    }
    
    public MagicWhenLeavesPlayTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenLeavesPlay;
    }
    
    public static final MagicWhenLeavesPlayTrigger Exile = new MagicWhenLeavesPlayTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
            if (permanent == act.getPermanent()) {
                act.setToLocation(MagicLocationType.Exile);
            }
            return MagicEvent.NONE;
        }
    };
    
    public static final MagicWhenLeavesPlayTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new MagicWhenLeavesPlayTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
                return filter.accept(permanent, permanent.getController(), act.getPermanent());
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
    
    public static final MagicWhenLeavesPlayTrigger createSelfOrAnother(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new MagicWhenLeavesPlayTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
                return permanent == act.getPermanent() || filter.accept(permanent, permanent.getController(), act.getPermanent());
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
    
    public static final MagicWhenLeavesPlayTrigger createAnother(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new MagicWhenLeavesPlayTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
                return permanent != act.getPermanent() && filter.accept(permanent, permanent.getController(), act.getPermanent());
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
