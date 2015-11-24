package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicPlayMod;
import magic.model.action.ReanimateAction;
import magic.model.event.MagicEvent;

public class PersistTrigger extends SelfDiesTrigger {

    private static final PersistTrigger INSTANCE = new PersistTrigger();

    private PersistTrigger() {}

    public static final PersistTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
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
        game.doAction(new ReanimateAction(
            card,
            card.getOwner(),
            MagicPlayMod.PERSIST
        ));
    }
}
