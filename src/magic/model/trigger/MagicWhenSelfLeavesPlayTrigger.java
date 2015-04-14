package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.RemoveFromPlayAction;
import magic.model.action.ReturnLinkedExileAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicWhenSelfLeavesPlayTrigger extends MagicWhenLeavesPlayTrigger {
    public MagicWhenSelfLeavesPlayTrigger(final int priority) {
        super(priority);
    }
    
    public MagicWhenSelfLeavesPlayTrigger() {}
   
    @Override
    public boolean accept(final MagicPermanent permanent, final RemoveFromPlayAction act) {
        return act.isPermanent(permanent);
    }
    
    public static final MagicWhenSelfLeavesPlayTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicWhenSelfLeavesPlayTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final RemoveFromPlayAction data) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }

    // replacement effect has priority 1
    public static final MagicWhenSelfLeavesPlayTrigger IfDieExileInstead = new MagicWhenSelfLeavesPlayTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final RemoveFromPlayAction act) {
            if (act.getToLocation() == MagicLocationType.Graveyard) {
                act.setToLocation(MagicLocationType.Exile);
            }
            return MagicEvent.NONE;
        }
    };
    
    public static final MagicWhenSelfLeavesPlayTrigger ExileUntilLeaves = new MagicWhenSelfLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final RemoveFromPlayAction act) {
            game.doAction(new ReturnLinkedExileAction(act.getPermanent(),MagicLocationType.Play));
            return MagicEvent.NONE;
        }
    };
}
