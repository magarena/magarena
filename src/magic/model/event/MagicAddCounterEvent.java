package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicTuple;
import magic.model.action.ChangeCountersAction;

public class MagicAddCounterEvent extends MagicEvent {

    public MagicAddCounterEvent(final MagicPermanent permanent,final MagicCounterType counterType,final int amount) {
        super(
            permanent,
            new MagicTuple(amount, counterType),
            EVENT_ACTION,
            genDescription(permanent,counterType,amount)
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        final MagicTuple tup = event.getRefTuple();
        game.doAction(new ChangeCountersAction(
            event.getSource(),
            event.getPermanent(),
            tup.getCounterType(1),
            tup.getInt(0)
        ));
    };

    private static String genDescription(final MagicPermanent permanent,final MagicCounterType counterType,final int amount) {
        final StringBuilder description=new StringBuilder("Put ");
        if (amount==1) {
            description.append("a ").append(counterType.getName()).append(" counter");
        } else {
            description.append(amount).append(' ').append(counterType.getName()).append(" counters");
        }
        description.append(" on SN.");
        return description.toString();
    }
}
