package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicCopyMap;
import magic.model.action.ChangeCountersAction;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicRemoveCounterEvent extends MagicEvent {

    private final MagicCondition cond;
    private final MagicCounterType ctype;

    public MagicRemoveCounterEvent(final MagicPermanent permanent,final MagicCounterType counterType,final int amount) {
        super(
            permanent,
            amount,
            EVENT_ACTION,
            genDescription(permanent,counterType,amount)
        );
        cond = MagicConditionFactory.CounterAtLeast(counterType, amount);
        ctype = counterType;
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        final MagicCounterType ctype = ((MagicRemoveCounterEvent)event).ctype;
        game.doAction(new ChangeCountersAction(
            event.getPermanent(),
            ctype,
            -event.getRefInt()
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
        description.append(" from ").append(permanent.getName()).append('.');
        return description.toString();
    }
    @Override
    public MagicEvent copy(final MagicCopyMap copyMap) {
        return new MagicRemoveCounterEvent(
            copyMap.copy(getPermanent()),
            ctype,
            getRefInt()
        );
    }
}
