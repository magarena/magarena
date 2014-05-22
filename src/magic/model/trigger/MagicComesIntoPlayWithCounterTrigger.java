package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;

public class MagicComesIntoPlayWithCounterTrigger extends MagicWhenComesIntoPlayTrigger {

    private final MagicCounterType counterType;
    private final int amount;

    public MagicComesIntoPlayWithCounterTrigger(final MagicCounterType counterType, final int amount) {
        super(MagicTrigger.REPLACEMENT);
        this.counterType = counterType;
        this.amount = amount;
    }
    
    public MagicComesIntoPlayWithCounterTrigger() {
        super(MagicTrigger.REPLACEMENT);
        this.counterType= MagicCounterType.PlusOne;
        this.amount=0;
    }
    
    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
        game.doAction(new MagicChangeCountersAction(
            permanent,
            counterType,
            amount,
            false
        ));
        return MagicEvent.NONE;
    }
    
    public static final MagicComesIntoPlayWithCounterTrigger XCounters(final MagicCounterType counterType) {
        return new MagicComesIntoPlayWithCounterTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
                game.doAction(new MagicChangeCountersAction(
                    permanent,
                    counterType,
                    payedCost.getX(),
                    false
                ));
                return MagicEvent.NONE;
            }
        };
    };
    
}
