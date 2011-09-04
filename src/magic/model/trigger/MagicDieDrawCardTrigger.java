package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.event.MagicDrawEvent;
import magic.model.event.MagicEvent;

//When C is put into a graveyard from the battlefield, you draw a card.
public class MagicDieDrawCardTrigger extends MagicWhenPutIntoGraveyardTrigger {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
        if (MagicLocationType.Play==triggerData.fromLocation) {
            return new MagicDrawEvent(permanent,permanent.getController(),1);
        }
        return MagicEvent.NONE;
    }
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
        //do nothing
    }
}
