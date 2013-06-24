package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPayedCost;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;

public class MagicComesIntoPlayWithCounterTrigger extends MagicWhenComesIntoPlayTrigger {

    private final MagicCounterType counterType;
    private final String description;
    private final int amount;

    public MagicComesIntoPlayWithCounterTrigger(
            final MagicCounterType counterType,
            final String description,
            final int amount) {
        this.counterType = counterType;
        this.description = description;
        this.amount = amount;
    }

    @Override
    public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPayedCost payedCost) {
        return new MagicEvent(
            permanent,
            this,
            amount > 1 ?
                "SN enters the battlefield with " + amount + " " + description + " counters on it." :
                "SN enters the battlefield with a " + description + " counter on it."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new MagicChangeCountersAction(
            event.getPermanent(),
            counterType,
            amount,
            false
        ));
    }
    @Override
    public boolean usesStack() {
        return false;
    }
}

