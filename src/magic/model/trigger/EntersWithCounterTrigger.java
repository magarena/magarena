package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicAmount;
import magic.model.MagicAmountFactory;
import magic.model.condition.MagicCondition;
import magic.model.action.ChangeCountersAction;
import magic.model.event.MagicEvent;

public class EntersWithCounterTrigger extends EntersBattlefieldTrigger {

    private final MagicCounterType counterType;
    private final int amount;
    private final MagicAmount count;
    private final MagicCondition cond;

    public EntersWithCounterTrigger(final MagicCounterType aCounterType, final int aAmount, final MagicAmount aCount, final MagicCondition aCond) {
        super(MagicTrigger.REPLACEMENT);
        counterType = aCounterType;
        amount = aAmount;
        count = aCount;
        cond = aCond;
    }

    public EntersWithCounterTrigger(final MagicCounterType aCounterType, final int aAmount, final MagicCondition aCond) {
        this(aCounterType, aAmount, MagicAmountFactory.One, aCond);
    }

    public EntersWithCounterTrigger(final MagicCounterType aCounterType, final int aAmount, final MagicAmount aCount) {
        this(aCounterType, aAmount, aCount, MagicCondition.NONE);
    }

    public EntersWithCounterTrigger(final MagicCounterType aCounterType, final int aAmount) {
        this(aCounterType, aAmount, MagicAmountFactory.One);
    }

    public EntersWithCounterTrigger() {
        this(MagicCounterType.PlusOne, 0, MagicAmountFactory.One);
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
        final int total = amount * count.getPositiveAmount(permanent, permanent.getController());
        if (cond.accept(permanent)) {
            game.doAction(ChangeCountersAction.Enters(
                permanent,
                counterType,
                total
            ));
        }
        return MagicEvent.NONE;
    }

    public static final EntersWithCounterTrigger XCounters(final MagicCounterType counterType) {
        return new EntersWithCounterTrigger() {
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

    public static final EntersWithCounterTrigger MultiKicker(final MagicCounterType counterType) {
        return new EntersWithCounterTrigger() {
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
