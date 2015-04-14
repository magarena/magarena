package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicType;
import magic.model.action.TapAction;
import magic.model.event.MagicEvent;

public class MagicTappedIntoPlayUnlessTwoTrigger extends MagicWhenComesIntoPlayTrigger {

    private static final MagicTappedIntoPlayUnlessTwoTrigger INSTANCE = new MagicTappedIntoPlayUnlessTwoTrigger();

    private MagicTappedIntoPlayUnlessTwoTrigger() {}

    public static MagicTappedIntoPlayUnlessTwoTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
        return (permanent.getController().getNrOfPermanents(MagicType.Land) > 3) ?
            new MagicEvent(
                permanent,
                this,
                "SN enters the battlefield tapped."
            ):
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(TapAction.Enters(event.getPermanent()));
    }

    @Override
    public boolean usesStack() {
        return false;
    }
}
