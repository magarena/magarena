package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicAmount;
import magic.model.MagicAmountFactory;
import magic.model.action.ChangeCountersAction;
import magic.model.event.MagicEvent;

public class MagicComesIntoPlayWithCounterTrigger extends MagicWhenComesIntoPlayTrigger {

    private final MagicCounterType counterType;
    private final int amount;
    private final MagicAmount count;

    public MagicComesIntoPlayWithCounterTrigger(final MagicCounterType aCounterType, final int aAmount, final MagicAmount aCount) {
        super(MagicTrigger.REPLACEMENT);
        counterType = aCounterType;
        amount = aAmount;
        count = aCount;
    }
    
    public MagicComesIntoPlayWithCounterTrigger(final MagicCounterType aCounterType, final int aAmount) {
        this(aCounterType, aAmount, MagicAmountFactory.One);
    }
    
    public MagicComesIntoPlayWithCounterTrigger() {
        this(MagicCounterType.PlusOne, 0, MagicAmountFactory.One);
    }
    
    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
        final int total = amount * count.getAmount(permanent);
        game.doAction(ChangeCountersAction.Enters(
            permanent,
            counterType,
            total
        ));
        return MagicEvent.NONE;
    }
    
    public static final MagicComesIntoPlayWithCounterTrigger XCounters(final MagicCounterType counterType) {
        return new MagicComesIntoPlayWithCounterTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
                game.doAction(ChangeCountersAction.Enters(
                    permanent,
                    counterType,
                    payedCost.getX()
                ));
                return MagicEvent.NONE;
            }
        };
    }
    
    public static final MagicComesIntoPlayWithCounterTrigger MultiKicker(final MagicCounterType counterType) {
        return new MagicComesIntoPlayWithCounterTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
                game.doAction(ChangeCountersAction.Enters(
                    permanent,
                    counterType,
                    payedCost.getKicker()
                ));
                return MagicEvent.NONE;
            }
        };
    }
    
}
