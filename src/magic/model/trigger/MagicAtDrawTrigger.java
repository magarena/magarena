package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicCounterType;
import magic.model.event.MagicEvent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicSimpleMayChoice;

public abstract class MagicAtDrawTrigger extends MagicTrigger<MagicPlayer> {
    public MagicAtDrawTrigger(final int priority) {
        super(priority); 
    }
    
    public MagicAtDrawTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.AtDraw;
    }
}
