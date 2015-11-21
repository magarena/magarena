package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.TapAction;
import magic.model.event.MagicEvent;

public class EntersTappedTrigger extends MagicWhenComesIntoPlayTrigger {

    private static final EntersTappedTrigger INSTANCE = new EntersTappedTrigger();

    private EntersTappedTrigger() {
        super(MagicTrigger.REPLACEMENT);
    }

    public static EntersTappedTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
        game.doAction(TapAction.Enters(permanent));
        return MagicEvent.NONE;
    }
}
