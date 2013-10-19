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

    public static MagicWhenDiesTrigger ReturnToOwnersHand = new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicMoveCardAction act) {
            return new MagicEvent(
                permanent.getCard(),
                this,
                "Return SN to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getCard();
            game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
            game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
        }
    };
}
