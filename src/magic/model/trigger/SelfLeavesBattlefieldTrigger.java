package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.RemoveFromPlayAction;
import magic.model.action.ReturnLinkedExileAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class SelfLeavesBattlefieldTrigger extends LeavesBattlefieldTrigger {
    public SelfLeavesBattlefieldTrigger(final int priority) {
        super(priority);
    }

    public SelfLeavesBattlefieldTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final RemoveFromPlayAction act) {
        return act.isPermanent(permanent);
    }

    public static final SelfLeavesBattlefieldTrigger create(final MagicSourceEvent sourceEvent) {
        return new SelfLeavesBattlefieldTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final RemoveFromPlayAction data) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }

    // replacement effect has priority 1
    public static final SelfLeavesBattlefieldTrigger IfDieExileInstead = new SelfLeavesBattlefieldTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final RemoveFromPlayAction act) {
            if (act.to(MagicLocationType.Graveyard)) {
                act.setToLocation(MagicLocationType.Exile);
            }
            return MagicEvent.NONE;
        }
    };

    public static final SelfLeavesBattlefieldTrigger ExileUntilLeaves = new SelfLeavesBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final RemoveFromPlayAction act) {
            game.doAction(new ReturnLinkedExileAction(act.getPermanent(),MagicLocationType.Play));
            return MagicEvent.NONE;
        }
    };
}
