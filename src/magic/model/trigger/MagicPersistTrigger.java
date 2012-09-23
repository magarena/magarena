package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.event.MagicEvent;

public class MagicPersistTrigger extends MagicWhenPutIntoGraveyardTrigger {

    private static final MagicPersistTrigger INSTANCE = new MagicPersistTrigger();

    private MagicPersistTrigger() {}
    
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
        return (triggerData.fromLocation==MagicLocationType.Play && 
                permanent.getCounters(MagicCounterType.MinusOne)==0) ?
            new MagicEvent(
                permanent,
                this,
                "Return SN to play under its owner's control with a -1/-1 counter on it."
            ):
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
        final MagicCard card = event.getPermanent().getCard();
        game.doAction(new MagicReanimateAction(card.getOwner(),card,MagicPlayCardAction.PERSIST));
    }
    
    public static final MagicPersistTrigger getInstance() {    
        return INSTANCE;
    }
}
