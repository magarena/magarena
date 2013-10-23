package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicLocationType;
import magic.model.target.MagicTargetPicker;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicRuleEventAction;
import magic.model.action.MagicRemoveFromPlayAction;

public abstract class MagicWhenLeavesPlayTrigger extends MagicTrigger<MagicRemoveFromPlayAction> {
    public MagicWhenLeavesPlayTrigger(final int priority) {
        super(priority);
    }
    
    public MagicWhenLeavesPlayTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenLeavesPlay;
    }
}
