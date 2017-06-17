package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicLocationType;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.action.RemoveFromPlayAction;

public abstract class LeavesBattlefieldTrigger extends MagicTrigger<RemoveFromPlayAction> {
    public LeavesBattlefieldTrigger(final int priority) {
        super(priority);
    }

    public LeavesBattlefieldTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenLeavesPlay;
    }

    public static final LeavesBattlefieldTrigger Exile = new LeavesBattlefieldTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final RemoveFromPlayAction act) {
            if (permanent == act.getPermanent()) {
                act.setToLocation(MagicLocationType.Exile);
            }
            return MagicEvent.NONE;
        }
    };

    public static final LeavesBattlefieldTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new LeavesBattlefieldTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final RemoveFromPlayAction act) {
                return filter.accept(permanent, permanent.getController(), act.getPermanent());
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final RemoveFromPlayAction act) {
                return sourceEvent.getTriggerEvent(permanent, act.getPermanent());
            }
        };
    }

    public static final LeavesBattlefieldTrigger createSelfOrAnother(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new LeavesBattlefieldTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final RemoveFromPlayAction act) {
                return permanent == act.getPermanent() || filter.accept(permanent, permanent.getController(), act.getPermanent());
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final RemoveFromPlayAction act) {
                return sourceEvent.getTriggerEvent(permanent, act.getPermanent());
            }
        };
    }

    public static final LeavesBattlefieldTrigger createAnother(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new LeavesBattlefieldTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final RemoveFromPlayAction act) {
                return permanent != act.getPermanent() && filter.accept(permanent, permanent.getController(), act.getPermanent());
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final RemoveFromPlayAction act) {
                return sourceEvent.getTriggerEvent(permanent, act.getPermanent());
            }
        };
    }
}
