package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicPlayMod;
import magic.model.action.MagicReanimateAction;
import magic.model.event.MagicEvent;

public class MagicPersistTrigger extends MagicWhenDiesTrigger {

    private static final MagicPersistTrigger INSTANCE = new MagicPersistTrigger();

    private MagicPersistTrigger() {}

    public static final MagicPersistTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent getEvent(final MagicPermanent permanent) {
        return permanent.getCounters(MagicCounterType.MinusOne) == 0 ?
            new MagicEvent(
                permanent,
                this,
                "Return SN to play under its owner's control with a -1/-1 counter on it."
            ):
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicCard card = event.getPermanent().getCard();
        game.doAction(new MagicReanimateAction(
            card,
            card.getOwner(),
            MagicPlayMod.PERSIST
        ));
    }
}
