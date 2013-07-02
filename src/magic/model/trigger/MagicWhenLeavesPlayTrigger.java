package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.MagicLocationType;
import magic.model.action.MagicRemoveFromPlayAction;

public abstract class MagicWhenLeavesPlayTrigger extends MagicTrigger<MagicRemoveFromPlayAction> {
    public MagicWhenLeavesPlayTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenLeavesPlayTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenLeavesPlay;
    }

    // replacement effect has priority 1
    public static final MagicWhenLeavesPlayTrigger IfDieExileInstead = new MagicWhenLeavesPlayTrigger(1) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicRemoveFromPlayAction act) {
            if (act.isPermanent(permanent) && act.getToLocation() == MagicLocationType.Graveyard) {
                act.setToLocation(MagicLocationType.Exile);
            }
            return MagicEvent.NONE;
        }
    };
}
