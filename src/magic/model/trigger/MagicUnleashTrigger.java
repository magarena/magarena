package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.ChangeCountersAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;

public class MagicUnleashTrigger extends MagicWhenComesIntoPlayTrigger {

    private static final MagicUnleashTrigger INSTANCE = new MagicUnleashTrigger();

    private MagicUnleashTrigger() {}

    public static MagicUnleashTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPayedCost payedCost) {
        return new MagicEvent(
            permanent,
            new MagicMayChoice(),
            this,
            "PN may$ have SN enter the battlefield with a +1/+1 counter on it."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isYes()) {
            game.doAction(ChangeCountersAction.Enters(
                event.getPermanent(),
                MagicCounterType.PlusOne,
                1
            ));
        }
    }
    @Override
    public boolean usesStack() {
        return false;
    }
}
