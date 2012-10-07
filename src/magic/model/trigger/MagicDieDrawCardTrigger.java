package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;

//When C is put into a graveyard from the battlefield, you may or must draw a card.
public class MagicDieDrawCardTrigger extends MagicWhenPutIntoGraveyardTrigger {
    
    private final boolean mustDraw;
    
    public MagicDieDrawCardTrigger(final boolean mandatory) {
        this.mustDraw = mandatory;
    }
    
    @Override
    public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicGraveyardTriggerData triggerData) {
        if (triggerData.fromLocation == MagicLocationType.Play) {
            return mustDraw ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN draws a card."
                ):
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.DRAW_CARDS,
                        1,
                        MagicSimpleMayChoice.DEFAULT_NONE
                    ),
                    this,
                    "PN may$ draw a card."
                );
        }
        return MagicEvent.NONE;
    }
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] choiceResults) {
        if (mustDraw) {
            game.doAction(new MagicDrawAction(event.getPlayer(),1));
        } else if (MagicMayChoice.isYesChoice(choiceResults[0])) {
            game.doAction(new MagicDrawAction(event.getPlayer(),1));
        }
    }
}
