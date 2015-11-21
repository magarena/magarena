package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.action.ChangeLifeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;

public class ExtortTrigger extends MagicWhenOtherSpellIsCastTrigger {

    private static final ExtortTrigger INSTANCE = new ExtortTrigger();

    private ExtortTrigger() {}

    public static final ExtortTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
        return permanent.isFriend(cardOnStack) ?
            new MagicEvent(
                permanent,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{W/B}"))
                ),
                this,
                "You may$ pay {W/B}. If you do, each opponent loses 1 life and you gain that much life."
            ):
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isYes()) {
            game.doAction(new ChangeLifeAction(event.getPlayer().getOpponent(),-1));
            game.doAction(new ChangeLifeAction(event.getPlayer(),1));
        }
    }
}
