package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicTapAction;
import magic.model.event.MagicEvent;

public class MagicTappedIntoPlayTrigger extends MagicWhenComesIntoPlayTrigger {

    private static final MagicTappedIntoPlayTrigger INSTANCE = new MagicTappedIntoPlayTrigger();

    private MagicTappedIntoPlayTrigger() {
        super(MagicTrigger.REPLACEMENT);
    }

    public static MagicTappedIntoPlayTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
        game.doAction(MagicTapAction.Enters(permanent));
        return MagicEvent.NONE;
    }
}
