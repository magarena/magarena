package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicCopyMap;
import magic.model.MagicTuple;
import magic.model.action.ChangeCountersAction;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicRemoveCounterEvent extends MagicEvent {

    private final MagicCondition cond;

    public MagicRemoveCounterEvent(final MagicPermanent permanent,final MagicCounterType counterType,final int amount) {
        super(
            permanent,
            new MagicTuple(amount, counterType),
            EVENT_ACTION,
            genDescription(permanent,counterType,amount)
        );
        cond = MagicConditionFactory.CounterAtLeast(counterType, amount);
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        final MagicTuple tup = event.getRefTuple();
        game.doAction(new ChangeCountersAction(
            event.getSource(),
            event.getPermanent(),
            tup.getCounterType(1),
            -tup.getInt(0)
        ));
    };

    @Override
    public boolean isSatisfied() {
        return cond.accept(getSource()) && super.isSatisfied();
    }

    private static String genDescription(final MagicPermanent permanent,final MagicCounterType counterType,final int amount) {
        final StringBuilder description=new StringBuilder("Remove ");
        if (amount==1) {
            description.append("a ").append(counterType.getName()).append(" counter");
        } else {
            description.append(amount).append(' ').append(counterType.getName()).append(" counters");
        }
        description.append(" from SN.");
        return description.toString();
    }

    @Override
    public MagicEvent copy(final MagicCopyMap copyMap) {
        final MagicTuple tup = getRefTuple();
        return new MagicRemoveCounterEvent(
            copyMap.copy(getPermanent()),
            tup.getCounterType(1),
            tup.getInt(0)
        );
    }
}
