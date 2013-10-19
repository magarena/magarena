package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicRuleEventAction;
import magic.model.target.MagicTargetPicker;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;

public abstract class MagicWhenDiesTrigger extends MagicWhenPutIntoGraveyardTrigger {
    public static final MagicWhenDiesTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicWhenDiesTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicMoveCardAction data) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
    
    public MagicWhenDiesTrigger(final int priority) {
        super(priority);
    }
    
    public MagicWhenDiesTrigger() {}
    
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicMoveCardAction act) {
        return super.accept(permanent,act) && act.fromLocation == MagicLocationType.Play;
    }
}
