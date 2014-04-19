package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicLocationType;
import magic.model.event.MagicEvent;
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
}
